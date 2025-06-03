package com.example.jsonplaceholderapi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapi.data.model.Post
import com.example.jsonplaceholderapi.data.model.User
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import com.example.jsonplaceholderapi.datastore.LikedPostsStore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PostWithUser(val post: Post, val user: User, val liked: Boolean = false)

sealed class PostsUiState {
    data object Loading : PostsUiState()
    data class Success(val postsWithUsers: List<PostWithUser>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}

class PostsViewModel(
    application: Application,
    private val repository: JsonPlaceholderRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState

    private val likedPosts = mutableSetOf<Int>()

    init {
        viewModelScope.launch {
            val likedFromStore = LikedPostsStore.likedPostIdsFlow(getApplication())
                .first()
                .mapNotNull { it.toIntOrNull() }
                .toSet()

            likedPosts.addAll(likedFromStore)
            loadPostsAndUsers()
        }
    }

    private fun loadPostsAndUsers() {
        viewModelScope.launch {
            _uiState.value = PostsUiState.Loading
            try {
                val (postsList, usersList) = coroutineScope {
                    val postsDeferred = async { repository.getPosts().first() }
                    val usersDeferred = async { repository.getUsers().first() }
                    Pair(postsDeferred.await(), usersDeferred.await())
                }

                val userMap = usersList.associateBy { it.id }

                val postsWithUsers = postsList.mapNotNull { post ->
                    userMap[post.userId]?.let { user ->
                        PostWithUser(post, user, likedPosts.contains(post.id))
                    }
                }.sortedByDescending { it.liked }

                _uiState.value = PostsUiState.Success(postsWithUsers)

            } catch (e: Exception) {
                _uiState.value = PostsUiState.Error(e.localizedMessage ?: "Błąd ładowania danych")
            }
        }
    }

    fun toggleLike(postId: Int) {
        val currentState = _uiState.value
        if (currentState is PostsUiState.Success) {
            if (likedPosts.contains(postId)) likedPosts.remove(postId) else likedPosts.add(postId)

            viewModelScope.launch {
                LikedPostsStore.saveLikedPosts(
                    getApplication(),
                    likedPosts.map { it.toString() }.toSet()
                )
            }

            val updated = currentState.postsWithUsers.map {
                if (it.post.id == postId) it.copy(liked = !it.liked) else it
            }.sortedByDescending { it.liked }

            _uiState.value = PostsUiState.Success(updated)
        }
    }

    fun getLikeCount(): Int = likedPosts.size
    fun getTotalCount(): Int = (_uiState.value as? PostsUiState.Success)?.postsWithUsers?.size ?: 0
    fun retry() = loadPostsAndUsers()
}
