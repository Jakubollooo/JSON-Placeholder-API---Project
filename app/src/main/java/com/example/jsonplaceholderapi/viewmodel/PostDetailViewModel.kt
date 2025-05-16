package com.example.jsonplaceholderapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapi.data.model.Post
import com.example.jsonplaceholderapi.data.model.User
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class PostDetailUiState {
    data object Loading : PostDetailUiState()
    data class Success(val post: Post, val user: User) : PostDetailUiState()
    data class Error(val message: String) : PostDetailUiState()
}

class PostDetailViewModel(private val repository: JsonPlaceholderRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState

    fun loadPost(postId: Int) {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            try {
                val post = repository.getPost(postId).first()
                val user = repository.getUser(post.userId).first()
                _uiState.value = PostDetailUiState.Success(post, user)
            } catch (e: Exception) {
                _uiState.value = PostDetailUiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}