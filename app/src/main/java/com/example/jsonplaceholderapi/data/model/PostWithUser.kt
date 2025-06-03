package com.example.jsonplaceholderapi.data.model

data class PostWithUser(
    val post: Post,
    val user: User,
    val liked: Boolean = false
)