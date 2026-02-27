package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.example.gigmarket.ui.components.NeonButton
import com.example.gigmarket.ui.components.NeonTextField
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.GigMarketTheme
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary

@Composable
fun WorkerSignupScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    // Animation states for glow
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "glowAlpha"
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Worker icon with glow
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .shadow(
                        elevation = 30.dp,
                        shape = CircleShape,
                        ambientColor = NeonCyan.copy(alpha = glowAlpha),
                        spotColor = NeonCyan.copy(alpha = glowAlpha)
                    )
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(NeonCyan, NeonPurple)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👷",
                    fontSize = 48.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Worker Signup",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Join the GigMarket workforce",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Full Name Input
            NeonTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "Full Name",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Number Input
            NeonTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Phone Number",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Input
            NeonTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Input
            NeonTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                isPassword = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Optional Skill Input
            NeonTextField(
                value = skill,
                onValueChange = { skill = it },
                placeholder = "Optional Skill (e.g., Plumber)",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // OTP Input
            NeonTextField(
                value = otp,
                onValueChange = { otp = it },
                placeholder = "Enter 6-digit OTP",
                maxLength = 6,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            NeonButton(
                text = "Register",
                onClick = {
                    // Dummy navigation to WorkerHomeScreen
                    navController.navigate("worker_home")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login link
            Text(
                text = "Already have an account? Login",
                style = MaterialTheme.typography.bodyMedium,
                color = NeonCyan,
                modifier = Modifier
                    .clickable { navController.navigate("worker_login") }
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0F1A)
@Composable
fun WorkerSignupScreenPreview() {
    GigMarketTheme {
        WorkerSignupScreen(navController = rememberNavController())
    }
}
