package com.example.gigmarket.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefs: SharedPreferences = application.getSharedPreferences(
        "theme_prefs", 
        Context.MODE_PRIVATE
    )
    
    private val _isDarkTheme = MutableStateFlow(loadSavedTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    private fun loadSavedTheme(): Boolean {
        return prefs.getBoolean("is_dark_theme", true) // Default to dark theme
    }
    
    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            prefs.edit().putBoolean("is_dark_theme", newTheme).apply()
        }
    }
    
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            _isDarkTheme.value = isDark
            prefs.edit().putBoolean("is_dark_theme", isDark).apply()
        }
    }
}

