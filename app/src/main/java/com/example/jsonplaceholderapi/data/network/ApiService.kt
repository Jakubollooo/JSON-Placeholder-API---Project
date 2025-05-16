package com.example.jsonplaceholderapi.data.network

import com.example.jsonplaceholderapi.data.model.Post
import com.example.jsonplaceholderapi.data.model.Todo
import com.example.jsonplaceholderapi.data.model.User
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{postId}")
    suspend fun getPost(@Path("postId") postId: Int): Post

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: Int): User

    @GET("users/{userId}/todos")
    suspend fun getUserTodos(@Path("userId") userId: Int): List<Todo>
}