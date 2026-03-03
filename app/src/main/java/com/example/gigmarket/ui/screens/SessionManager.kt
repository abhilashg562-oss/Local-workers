package com.example.gigmarket.ui.screens

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember

// Session Manager to track login/logout state
// This ensures requests list resets when user logs in again after logout
object SessionManager {
    private var _sessionId = 0
    val sessionId: Int get() = _sessionId
    
    fun onLogin() {
        _sessionId++
    }
    
    fun onLogout() {
        _sessionId++
    }
}
