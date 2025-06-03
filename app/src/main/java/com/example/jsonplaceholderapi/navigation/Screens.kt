package com.example.jsonplaceholderapi.navigation

sealed class Screen(val route: String) {
    data object Posts : Screen("posts")
    data object Profile : Screen("profile")
    data object PostDetail : Screen("posts/{postId}") {
        fun createRoute(postId: Int) = "posts/$postId"
    }
    data object UserDetail : Screen("users/{userId}") {
        fun createRoute(userId: Int) = "users/$userId"
    }
}