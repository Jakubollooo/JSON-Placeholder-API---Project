package com.example.jsonplaceholderapi.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "liked_posts")

object LikedPostsStore {

    private val LIKED_POSTS_KEY = stringSetPreferencesKey("liked_posts")

    fun likedPostIdsFlow(context: Context): Flow<Set<String>> =
        context.dataStore.data.map { prefs -> prefs[LIKED_POSTS_KEY] ?: emptySet() }

    suspend fun saveLikedPosts(context: Context, postIds: Set<String>) {
        context.dataStore.edit { prefs ->
            prefs[LIKED_POSTS_KEY] = postIds
        }
    }
}
