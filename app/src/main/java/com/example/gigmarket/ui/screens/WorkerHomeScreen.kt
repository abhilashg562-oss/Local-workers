package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gigmarket.ui.components.NeonRadialGlowBackground
import com.example.gigmarket.ui.components.WorkerBottomNavBar
import com.example.gigmarket.ui.theme.GigMarketTheme
import com.example.gigmarket.ui.theme.NeonBg1
import com.example.gigmarket.ui.theme.NeonBg2
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonPink
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextPrimary
import com.example.gigmarket.ui.theme.TextSecondary

// ─── Main Dashboard Screen ─────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(navController: NavController) {
    var isOnline by remember { mutableStateOf(false) }
    var selectedNavIndex by remember { mutableIntStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "dashGlow")
    val glowAlpha: Float by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(NeonBg1, NeonBg2)
                )
            )
    ) {
        NeonRadialGlowBackground(glowAlpha = glowAlpha)

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                WorkerTopAppBar(navController = navController)
            },
            bottomBar = {
                WorkerBottomNavBar(
                    selectedIndex = selectedNavIndex,
                    onItemSelected = { index: Int ->
                        selectedNavIndex = index
                        when (index) {
                            0 -> { /* already on home */ }
                            1 -> navController.navigate("worker_requests")
                            2 -> navController.navigate("worker_settings")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Worker Dashboard",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = if (isOnline) "You are currently online" else "You are currently offline",
                    fontSize = 13.sp,
                    color = if (isOnline) NeonGreen.copy(alpha = 0.85f) else TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )

                Spacer(modifier = Modifier.height(36.dp))

                OnlineStatusButton(
                    isOnline = isOnline,
                    onToggle = { isOnline = !isOnline }
                )

                Spacer(modifier = Modifier.height(40.dp))

                RatingSummaryCard(
                    onViewHistoryClick = { navController.navigate("worker_review_history") }
                )
            }
        }
    }
}

// ─── Top App Bar ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerTopAppBar(navController: NavController, showBackArrow: Boolean = false) {
    TopAppBar(
        title = {},
        navigationIcon = {
            if (showBackArrow) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = NeonCyan
                    )
                }
            }
        },
        actions = {
            NeonIconButton(
                onClick = { navController.navigate("worker_language") },
                glowColor = NeonCyan
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
            }

            NeonIconButton(
                onClick = { navController.navigate("worker_earnings") },
                glowColor = NeonGreen
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Earnings",
                    tint = NeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NeonBg1.copy(alpha = 0.95f)
        )
    )
}

// ─── Neon Icon Button ──────────────────────────────────────────────────────────

@Composable
fun NeonIconButton(
    onClick: () -> Unit,
    glowColor: Color,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val glowElevation: Float by animateFloatAsState(
        targetValue = if (isPressed) 16f else 0f,
        animationSpec = tween(150),
        label = "iconGlow"
    )
    val scale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = tween(100),
        label = "iconScale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .shadow(
                elevation = glowElevation.dp,
                shape = CircleShape,
                ambientColor = glowColor.copy(alpha = 0.7f),
                spotColor = glowColor.copy(alpha = 0.7f)
            )
            .size(48.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

// ─── Online Status Button ──────────────────────────────────────────────────────

@Composable
fun OnlineStatusButton(
    isOnline: Boolean,
    onToggle: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "onlineGlow")
    val pulseGlow: Float by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = if (isOnline) 0.95f else 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val buttonGradient = if (isOnline) {
        Brush.horizontalGradient(
            colors = listOf(NeonPink, NeonPurple)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.08f),
                Color.White.copy(alpha = 0.12f)
            )
        )
    }

    val borderBrush = if (isOnline) {
        Brush.horizontalGradient(
            colors = listOf(NeonPink.copy(alpha = 0.9f), NeonPurple.copy(alpha = 0.7f))
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(NeonCyan.copy(alpha = 0.4f), NeonPurple.copy(alpha = 0.3f))
        )
    }

    val glowColor = if (isOnline) NeonPink else NeonCyan

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .shadow(
                    elevation = if (isOnline) 12.dp else 0.dp,
                    shape = CircleShape,
                    ambientColor = if (isOnline) NeonGreen else Color.Transparent,
                    spotColor = if (isOnline) NeonGreen else Color.Transparent
                )
                .background(
                    color = if (isOnline) NeonGreen else TextSecondary,
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .widthIn(min = 200.dp, max = 280.dp)
                .height(64.dp)
                .scale(scale)
                .shadow(
                    elevation = if (isOnline) 28.dp else 8.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = glowColor.copy(alpha = pulseGlow * 0.6f),
                    spotColor = glowColor.copy(alpha = pulseGlow * 0.6f)
                )
                .background(
                    brush = buttonGradient,
                    shape = RoundedCornerShape(32.dp)
                )
                .border(
                    width = 1.5.dp,
                    brush = borderBrush,
                    shape = RoundedCornerShape(32.dp)
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onToggle
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (isOnline) Color.White.copy(alpha = 0.9f)
                            else NeonCyan.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                )
                Text(
                    text = if (isOnline) "Go Offline" else "Go Online",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isOnline) Color.White else NeonCyan,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ─── Rating Summary Card ───────────────────────────────────────────────────────

@Composable
fun RatingSummaryCard(onViewHistoryClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val btnScale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    Box(
        modifier = Modifier
            .widthIn(max = 380.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonCyan.copy(alpha = 0.25f),
                spotColor = NeonPurple.copy(alpha = 0.2f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.10f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.5f),
                        NeonPurple.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Average Rating",
                fontSize = 13.sp,
                color = TextSecondary,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "4.6",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = NeonCyan,
                    modifier = Modifier
                        .size(36.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(4.dp),
                            ambientColor = NeonCyan.copy(alpha = 0.6f),
                            spotColor = NeonCyan.copy(alpha = 0.6f)
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Based on 124 reviews",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = NeonCyan.copy(alpha = 0.15f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .scale(btnScale)
                    .shadow(
                        elevation = 14.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = NeonCyan.copy(alpha = 0.4f),
                        spotColor = NeonPurple.copy(alpha = 0.3f)
                    )
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.15f),
                                NeonPurple.copy(alpha = 0.20f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.6f),
                                NeonPurple.copy(alpha = 0.5f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onViewHistoryClick
                    )
                    .padding(horizontal = 28.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "View History",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonCyan,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0F0F1A)
@Composable
fun WorkerHomeScreenPreview() {
    GigMarketTheme {
        WorkerHomeScreen(navController = rememberNavController())
    }
}
