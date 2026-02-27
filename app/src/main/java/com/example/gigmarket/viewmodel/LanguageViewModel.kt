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

enum class AppLanguage(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    HINDI("hi", "हिन्दी"),
    KANNADA("kn", "ಕನ್ನಡ")
}

class LanguageViewModel(application: Application) : AndroidViewModel(application) {
    
    private val prefs: SharedPreferences = application.getSharedPreferences(
        "language_prefs", 
        Context.MODE_PRIVATE
    )
    
    private val _currentLanguage = MutableStateFlow(loadSavedLanguage())
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()
    
    private fun loadSavedLanguage(): AppLanguage {
        val savedCode = prefs.getString("language_code", AppLanguage.ENGLISH.code) 
            ?: AppLanguage.ENGLISH.code
        return AppLanguage.entries.find { it.code == savedCode } ?: AppLanguage.ENGLISH
    }
    
    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            _currentLanguage.value = language
            prefs.edit().putString("language_code", language.code).apply()
        }
    }
    
    fun getLocalizedString(context: Context, resId: Int): String {
        val lang = _currentLanguage.value
        val config = context.resources.configuration
        val locale = java.util.Locale(lang.code)
        java.util.Locale.setDefault(locale)
        config.setLocale(locale)
        
        return context.createConfigurationContext(config).getString(resId)
    }
}

