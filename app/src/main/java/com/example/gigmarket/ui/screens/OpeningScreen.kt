package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonOrange
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary

@Composable
fun OpeningScreen(navController: NavController) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkBackgroundSecondary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Lightning Icon with Glow Animation
            Box(
                modifier = Modifier
                    .scale(scale)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(50),
                        ambientColor = NeonOrange.copy(alpha = glowAlpha),
                        spotColor = NeonOrange.copy(alpha = glowAlpha)
                    )
            ) {
                Text(
                    text = "⚡",
                    fontSize = 64.sp,
                    modifier = Modifier
                        .shadow(
                            elevation = 30.dp,
                            ambientColor = NeonOrange.copy(alpha = glowAlpha),
                            spotColor = NeonOrange.copy(alpha = glowAlpha)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Name with Gradient
            Text(
                text = "GigMarket",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(NeonCyan, NeonPurple)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "CONNECT. WORK. THRIVE.",
                style = MaterialTheme.typography.displayMedium.copy(
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Light
                ),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Customer Card
            RoleCard(
                icon = "👤",
                iconGlow = NeonPurple,
                title = "I need help",
                subtitle = "Find skilled workers near you",
                buttonText = "Get Started →",
                buttonGlow = NeonCyan,
                onClick = { navController.navigate("/user-login") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Worker Card
            RoleCard(
                icon = "🔧",
                iconGlow = NeonPurple,
                title = "I'm a worker",
                subtitle = "Find gigs in your area",
                buttonText = "Start Earning →",
                buttonGlow = NeonPurple,
                onClick = { navController.navigate("worker_login") }
            )
        }
    }
}

@Composable
fun RoleCard(
    icon: String,
    iconGlow: Color,
    title: String,
    subtitle: String,
    buttonText: String,
    buttonGlow: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale = if (isPressed) 0.98f else 1f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clip(RoundedCornerShape(24.dp))
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.White.copy(alpha = 0.1f),
                spotColor = Color.White.copy(alpha = 0.1f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.2f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon with glow
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(50),
                        ambientColor = iconGlow.copy(alpha = 0.5f),
                        spotColor = iconGlow.copy(alpha = 0.5f)
                    )
            ) {
                Text(
                    text = icon,
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Neon underline accent
            Box(
                modifier = Modifier
                    .height(2.dp)
                    .fillMaxWidth(0.3f)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(buttonGlow.copy(alpha = 0f), buttonGlow, buttonGlow.copy(alpha = 0f))
                        ),
                        shape = RoundedCornerShape(1.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button with glow effect
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(25.dp),
                        ambientColor = buttonGlow.copy(alpha = 0.3f),
                        spotColor = buttonGlow.copy(alpha = 0.5f)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonGlow.copy(alpha = 0.2f),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }
        }
    }
}

