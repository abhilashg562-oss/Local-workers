package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gigmarket.ui.components.LocalLinkLogo
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // Fade-in state
    var visible by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "splashFade"
    )

    // Trigger fade-in then navigate after 2s total
    LaunchedEffect(Unit) {
        visible = true
        delay(2000L)
        navController.navigate("/") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        DarkBackgroundSecondary,
                        DarkBackground
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        LocalLinkLogo(
            modifier = Modifier.alpha(alpha),
            fontSize = 48.sp,
            taglineFontSize = 15.sp,
            showTagline = true
        )
    }
}

