package com.example.jsonplaceholderapi.ui.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onUserClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista Postów") },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Mój profil")
                    }
                }
            )
        }
    ) { padding ->
        when (val state = uiState.value) {
            is PostsUiState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(padding))
            is PostsUiState.Error -> ErrorView(state.message) { viewModel.retry() }
            is PostsUiState.Success -> {
                Column(modifier = Modifier.padding(padding)) {

                    val liked = viewModel.getLikeCount()
                    val total = viewModel.getTotalCount()

                    Text(
                        text = "Polubione posty: $liked / $total",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
                        items(state.postsWithUsers) { postWithUser ->
                            PostItem(
                                postWithUser = postWithUser,
                                onPostClick = { onPostClick(postWithUser.post.id) },
                                onUserClick = { onUserClick(postWithUser.user.id) },
                                onLikeToggle = { viewModel.toggleLike(postWithUser.post.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
