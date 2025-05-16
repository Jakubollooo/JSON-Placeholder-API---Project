package com.example.jsonplaceholderapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapi.data.model.Post
import com.example.jsonplaceholderapi.data.model.User
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PostWithUser(val post: Post, val user: User)

sealed class PostsUiState {
    data object Loading : PostsUiState()
    data class Success(val postsWithUsers: List<PostWithUser>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}

class PostsViewModel(private val repository: JsonPlaceholderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState

    init {
        loadPostsAndUsers()
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
                        PostWithUser(post, user)
                    }

                }

                if (postsList.isNotEmpty() && postsWithUsers.isEmpty()) {
                    _uiState.value = PostsUiState.Error("Nie znaleziono pasujących danych użytkownika dla postów.")
                } else {
                    _uiState.value = PostsUiState.Success(postsWithUsers)
                }

            } catch (e: Exception) {
                _uiState.value = PostsUiState.Error(e.localizedMessage ?: "Wystąpił nieznany błąd podczas ładowania danych.")
            }
        }
    }

    fun retry() {
        loadPostsAndUsers()
    }
}