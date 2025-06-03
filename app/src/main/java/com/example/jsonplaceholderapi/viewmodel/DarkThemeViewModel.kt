package com.example.jsonplaceholderapi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jsonplaceholderapi.datastore.DarkThemePreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DarkThemeViewModel(application: Application) : AndroidViewModel(application) {

    val isDarkTheme: StateFlow<Boolean> = DarkThemePreference
        .isDarkModeFlow(application.applicationContext)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun toggleTheme(enabled: Boolean) {
        viewModelScope.launch {
            DarkThemePreference.setDarkMode(getApplication(), enabled)
        }
    }
}
