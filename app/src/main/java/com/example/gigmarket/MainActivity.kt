package com.example.gigmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gigmarket.ui.screens.OpeningScreen
import com.example.gigmarket.ui.screens.SplashScreen
import com.example.gigmarket.ui.screens.UserLogin
import com.example.gigmarket.ui.screens.UserDashboard
import com.example.gigmarket.ui.screens.WorkerLoginScreen
import com.example.gigmarket.ui.screens.WorkerSignupScreen
import com.example.gigmarket.ui.screens.WorkerHomeScreen
import com.example.gigmarket.ui.screens.WorkerLanguageScreen
import com.example.gigmarket.ui.screens.WorkerEarningsScreen
import com.example.gigmarket.ui.screens.WorkerRequestsScreen
import com.example.gigmarket.ui.screens.WorkerPendingWorksScreen
import com.example.gigmarket.ui.screens.WorkerSettingsScreen
import com.example.gigmarket.ui.screens.WorkerReviewHistoryScreen
import com.example.gigmarket.ui.screens.WorkerProfileScreen
import com.example.gigmarket.ui.screens.WorkerPrivacyScreen
import com.example.gigmarket.ui.screens.WorkerPersonalizationScreen
import com.example.gigmarket.ui.screens.WorkerUpdateScreen
import com.example.gigmarket.LoginPlaceholder
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
                        startDestination = "splash"
                    ) {
                        // 1️⃣ Splash Screen
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }
                        // 2️⃣ Landing Page
                        composable("/") {
                            OpeningScreen(navController = navController)
                        }
                        // 3️⃣ User Login
                        composable("/user-login") {
                            UserLogin(navController = navController)
                        }
                        composable(
                            route = "/user-dashboard/{userName}",
                            arguments = listOf(
                                navArgument("userName") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                }
                            )
                        ) { backStackEntry ->
                            val userName = backStackEntry.arguments?.getString("userName") ?: ""
                            UserDashboard(navController = navController, userName = userName)
                        }
                        composable("/provider-login") {
                            // Placeholder for Provider Login Screen
                            LoginPlaceholder(navController = navController, role = "Worker")
                        }
                        composable(
                            route = "worker_login",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerLoginScreen(navController = navController)
                        }
                        composable(
                            route = "worker_signup",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerSignupScreen(navController = navController)
                        }
                        composable(
                            route = "worker_home",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerHomeScreen(navController = navController)
                        }
                        composable(
                            route = "worker_language",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerLanguageScreen(navController = navController)
                        }
                        composable(
                            route = "worker_earnings",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerEarningsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_requests",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerRequestsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_pending_works",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPendingWorksScreen(navController = navController)
                        }
                        composable(
                            route = "worker_settings",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerSettingsScreen(navController = navController)
                        }
                        composable(
                            route = "worker_review_history",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerReviewHistoryScreen(navController = navController)
                        }
                        composable(
                            route = "worker_profile",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerProfileScreen(navController = navController)
                        }
                        composable(
                            route = "worker_privacy",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPrivacyScreen(navController = navController)
                        }
                        composable(
                            route = "worker_personalization",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerPersonalizationScreen(navController = navController)
                        }
                        composable(
                            route = "worker_update",
                            enterTransition = {
                                fadeIn(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideInVertically(
                                    initialOffsetY = { 30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            },
                            exitTransition = {
                                fadeOut(animationSpec = tween(350, easing = FastOutSlowInEasing)) +
                                slideOutVertically(
                                    targetOffsetY = { -30 },
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        ) {
                            WorkerUpdateScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

