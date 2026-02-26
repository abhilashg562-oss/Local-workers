package com.example.gigmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gigmarket.ui.screens.OpeningScreen
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.GigMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GigMarketTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "/"
                    ) {
                        composable("/") {
                            OpeningScreen(navController = navController)
                        }
                        composable("/user-login") {
                            // Placeholder for User Login Screen
                            LoginPlaceholder(navController = navController, role = "Customer")
                        }
                        composable("/provider-login") {
                            // Placeholder for Provider Login Screen
                            LoginPlaceholder(navController = navController, role = "Worker")
                        }
                    }
                }
            }
        }
    }
}

