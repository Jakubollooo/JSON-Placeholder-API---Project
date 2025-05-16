package com.example.jsonplaceholderapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jsonplaceholderapi.ui.postdetail.PostDetailScreen
import com.example.jsonplaceholderapi.ui.posts.PostsScreen
import com.example.jsonplaceholderapi.ui.userdetail.UserDetailScreen
import com.example.jsonplaceholderapi.viewmodel.*

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    postsViewModel: PostsViewModel,
    postDetailViewModel: PostDetailViewModel,
    userDetailViewModel: UserDetailViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Posts.route) {
        composable(Screen.Posts.route) {
            PostsScreen(
                viewModel = postsViewModel,
                onPostClick = { postId ->
                    navController.navigate(Screen.PostDetail.createRoute(postId))
                },
                onUserClick = { userId ->
                    navController.navigate(Screen.UserDetail.createRoute(userId))
                }
            )
        }
        composable(
            Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            PostDetailScreen(
                postId = postId,
                viewModel = postDetailViewModel,
                onBackClick = { navController.navigateUp() },
                onUserClick = { userId ->
                    navController.navigate(Screen.UserDetail.createRoute(userId))
                }
            )
        }
        composable(
            Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            UserDetailScreen(
                userId = userId,
                viewModel = userDetailViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }
    }
}