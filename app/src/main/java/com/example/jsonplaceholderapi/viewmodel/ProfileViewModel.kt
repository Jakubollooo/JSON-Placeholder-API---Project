package com.example.jsonplaceholderapi.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore(name = "user_profile")

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val firstNameKey = stringPreferencesKey("first_name")
    private val lastNameKey = stringPreferencesKey("last_name")
    private val photoPathKey = stringPreferencesKey("photo_path")

    private val ds = application.applicationContext.dataStore

    val firstName: Flow<String> = ds.data.map { it[firstNameKey] ?: "" }
    val lastName: Flow<String> = ds.data.map { it[lastNameKey] ?: "" }
    val photoPath: Flow<String> = ds.data.map { it[photoPathKey] ?: "" }

    fun saveName(first: String, last: String) = viewModelScope.launch {
        ds.edit { prefs ->
            prefs[firstNameKey] = first
            prefs[lastNameKey] = last
        }
    }

    fun savePhotoPath(path: String) = viewModelScope.launch {
        ds.edit { prefs -> prefs[photoPathKey] = path }
    }

    fun clearData() = viewModelScope.launch {
        ds.edit { it.clear() }
    }
}
