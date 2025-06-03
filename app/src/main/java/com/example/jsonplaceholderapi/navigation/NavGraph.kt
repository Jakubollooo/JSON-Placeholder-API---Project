package com.example.jsonplaceholderapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jsonplaceholderapi.ui.posts.PostsScreen
import com.example.jsonplaceholderapi.ui.postdetail.PostDetailScreen
import com.example.jsonplaceholderapi.ui.userdetail.UserDetailScreen
import com.example.jsonplaceholderapi.ui.profile.ProfileScreen
import com.example.jsonplaceholderapi.viewmodel.PostsViewModel
import com.example.jsonplaceholderapi.viewmodel.PostDetailViewModel
import com.example.jsonplaceholderapi.viewmodel.UserDetailViewModel

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
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: return@composable
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
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: return@composable
            UserDetailScreen(
                userId = userId,
                viewModel = userDetailViewModel,
                onBackClick = { navController.navigateUp() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                postsViewModel = postsViewModel
            )
        }
    }
}
