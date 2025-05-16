package com.example.jsonplaceholderapi.ui.posts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.jsonplaceholderapi.ui.posts.components.ErrorView
import com.example.jsonplaceholderapi.ui.posts.components.PostItem
import com.example.jsonplaceholderapi.viewmodel.PostWithUser
import com.example.jsonplaceholderapi.viewmodel.PostsUiState
import com.example.jsonplaceholderapi.viewmodel.PostsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    viewModel: PostsViewModel,
    onPostClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista Postów") })
        }
    ) { padding ->
        when (val state = uiState.value) {
            is PostsUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(padding))
            }
            is PostsUiState.Error -> {
                ErrorView(
                    message = state.message,
                    onRetry = { viewModel.retry() }
                )
            }
            is PostsUiState.Success -> {
                PostsList(
                    postsWithUsers = state.postsWithUsers,
                    onPostClick = onPostClick,
                    onUserClick = onUserClick,
                    paddingValues = padding
                )
            }
        }
    }
}

@Composable
fun PostsList(
    postsWithUsers: List<PostWithUser>,
    onPostClick: (Int) -> Unit,
    onUserClick: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(contentPadding = paddingValues) {
        items(postsWithUsers) { postWithUser ->
            PostItem(
                postWithUser = postWithUser,
                onPostClick = { onPostClick(postWithUser.post.id) },
                onUserClick = { onUserClick(postWithUser.user.id) }
            )
        }
    }
}
