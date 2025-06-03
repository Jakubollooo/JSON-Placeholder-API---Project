package com.example.jsonplaceholderapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import com.example.jsonplaceholderapi.data.network.RetrofitClient
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import com.example.jsonplaceholderapi.navigation.AppNavGraph
import com.example.jsonplaceholderapi.ui.theme.JsonPlaceholderAppTheme
import com.example.jsonplaceholderapi.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val darkThemeViewModel: DarkThemeViewModel = viewModel()
            val isDarkMode by darkThemeViewModel.isDarkTheme.collectAsState()

            val repository = remember { JsonPlaceholderRepository(RetrofitClient.apiService) }

            val postsViewModel: PostsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PostsViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            })

            val postDetailViewModel: PostDetailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PostDetailViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PostDetailViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            })

            val userDetailViewModel: UserDetailViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return UserDetailViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            })

            JsonPlaceholderAppTheme(darkTheme = isDarkMode) {
                AppNavGraph(
                    postsViewModel = postsViewModel,
                    postDetailViewModel = postDetailViewModel,
                    userDetailViewModel = userDetailViewModel
                )
            }
        }
    }
}
