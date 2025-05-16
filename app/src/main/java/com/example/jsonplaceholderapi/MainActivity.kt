package com.example.jsonplaceholderapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jsonplaceholderapi.data.network.RetrofitClient
import com.example.jsonplaceholderapi.data.repository.JsonPlaceholderRepository
import com.example.jsonplaceholderapi.navigation.AppNavGraph
import com.example.jsonplaceholderapi.ui.theme.JsonPlaceholderAppTheme
import com.example.jsonplaceholderapi.viewmodel.PostDetailViewModel
import com.example.jsonplaceholderapi.viewmodel.PostsViewModel
import com.example.jsonplaceholderapi.viewmodel.UserDetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JsonPlaceholderAppTheme {
                val repository = remember { JsonPlaceholderRepository(RetrofitClient.apiService) }


                val postsViewModel: PostsViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(PostsViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return PostsViewModel(repository) as T
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

                AppNavGraph(
                    postsViewModel = postsViewModel,
                    postDetailViewModel = postDetailViewModel,
                    userDetailViewModel = userDetailViewModel
                )
            }
        }
    }
}