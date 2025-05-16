package com.example.jsonplaceholderapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapi.data.model.Todo
import com.example.jsonplaceholderapi.data.model.User
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


sealed class UserUiState {
    data object Loading : UserUiState()
    data class Success(val user: User) : UserUiState()
    data class Error(val message: String) : UserUiState()
}

sealed class TodosUiState {
    data object Loading : TodosUiState()
    data class Success(val todos: List<Todo>) : TodosUiState()
    data class Error(val message: String) : TodosUiState()
}

class UserDetailViewModel(private val repository: JsonPlaceholderRepository) : ViewModel() {

    private val _userState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val userState: StateFlow<UserUiState> = _userState

    private val _todosState = MutableStateFlow<TodosUiState>(TodosUiState.Loading)
    val todosState: StateFlow<TodosUiState> = _todosState

    private fun loadUser(userId: Int) {
        viewModelScope.launch {
            _userState.value = UserUiState.Loading
            try {
                val user = repository.getUser(userId).first()
                _userState.value = UserUiState.Success(user)
            } catch (e: Exception) {
                _userState.value = UserUiState.Error(e.localizedMessage ?: "Błąd ładowania danych użytkownika")
            }
        }
    }

    private fun loadUserTodos(userId: Int) {
        viewModelScope.launch {
            _todosState.value = TodosUiState.Loading
            try {
                val todos = repository.getUserTodos(userId).first()
                _todosState.value = TodosUiState.Success(todos)
            } catch (e: Exception) {
                _todosState.value = TodosUiState.Error(e.localizedMessage ?: "Błąd ładowania listy zadań")
            }
        }
    }

    fun loadUserDetails(userId: Int) {
        viewModelScope.launch {
            _userState.value = UserUiState.Loading
            _todosState.value = TodosUiState.Loading

            try {
                coroutineScope {
                    val userDeferred = async { repository.getUser(userId).first() }
                    val todosDeferred = async { repository.getUserTodos(userId).first() }
                    _userState.value = UserUiState.Success(userDeferred.await())
                    _todosState.value = TodosUiState.Success(todosDeferred.await())
                }
            } catch (e: Exception) {
                val errorMessage = e.localizedMessage ?: "Wystąpił błąd podczas ładowania szczegółów"
                if (_userState.value is UserUiState.Loading) {
                    _userState.value = UserUiState.Error(errorMessage)
                }
                if (_todosState.value is TodosUiState.Loading) {
                    _todosState.value = TodosUiState.Error(errorMessage)
                }
            }
        }
    }

    fun retryUser(userId: Int) {
        loadUser(userId)
    }

    fun retryTodos(userId: Int) {
        loadUserTodos(userId)
    }
}