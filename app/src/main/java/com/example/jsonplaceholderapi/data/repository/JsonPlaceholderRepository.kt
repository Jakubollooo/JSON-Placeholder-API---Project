package com.example.jsonplaceholderapi.data.repository

import com.example.jsonplaceholderapi.data.model.Post
import com.example.jsonplaceholderapi.data.model.Todo
import com.example.jsonplaceholderapi.data.model.User
import com.example.jsonplaceholderapi.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class JsonPlaceholderRepository(private val apiService: ApiService) {

    fun getPosts(): Flow<List<Post>> = flow {
        emit(apiService.getPosts())
    }

    fun getUsers(): Flow<List<User>> = flow {
        emit(apiService.getUsers())
    }

    fun getPost(postId: Int): Flow<Post> = flow {
        emit(apiService.getPost(postId))
    }

    fun getUser(userId: Int): Flow<User> = flow {
        emit(apiService.getUser(userId))
    }

    fun getUserTodos(userId: Int): Flow<List<Todo>> = flow {
        emit(apiService.getUserTodos(userId))
    }
}