package com.example.jsonplaceholderapi.ui.posts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jsonplaceholderapi.viewmodel.PostWithUser

@Composable
fun PostItem(
    postWithUser: PostWithUser,
    onPostClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = postWithUser.post.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onPostClick() }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.clickable { onUserClick() }
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = postWithUser.user.name, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}