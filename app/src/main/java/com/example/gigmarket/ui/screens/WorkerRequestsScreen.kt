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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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

// ─── Data Models ─────────────────────────────────────────────────────────────

private data class JobRequest(
    val clientName: String,
    val location: String,
    val jobDescription: String,
    val offeredPrice: String,
    val requestedTime: String,
    val isAccepted: Boolean = false
)

// Using mutable state list to manage job requests locally
private val initialJobRequests = listOf(
    JobRequest(
        clientName = "Rahul Sharma",
        location = "HSR Layout, Bangalore",
        jobDescription = "Need to fix electrical wiring in kitchen. Switch not working.",
        offeredPrice = "₹ 500",
        requestedTime = "Within 2 hours"
    ),
    JobRequest(
        clientName = "Priya Menon",
        location = "Koramangala, Bangalore",
        jobDescription = "Install new ceiling fan with regulator. Have all materials ready.",
        offeredPrice = "₹ 800",
        requestedTime = "Today evening"
    ),
    JobRequest(
        clientName = "Arjun Patel",
        location = "Whitefield, Bangalore",
        jobDescription = "AC not cooling properly. Need inspection and gas refill if required.",
        offeredPrice = "₹ 1,500",
        requestedTime = "Tomorrow morning"
    ),
    JobRequest(
        clientName = "Sneha Rao",
        location = "Indiranagar, Bangalore",
        jobDescription = "Bathroom light flickering. May need to replace the entire fixture.",
        offeredPrice = "₹ 450",
        requestedTime = "Within 1 hour"
    ),
    JobRequest(
        clientName = "Vikram Singh",
        location = "MG Road, Bangalore",
        jobDescription = "Need complete electrical panel upgrade for 2BHK apartment.",
        offeredPrice = "₹ 3,500",
        requestedTime = "This weekend"
    )
)

// ─── Main Requests Screen ───────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerRequestsScreen(navController: NavController) {
    var selectedNavIndex by remember { mutableIntStateOf(1) }
    
// Local state for job requests using mutable state list
    // Key by SessionManager.sessionId to reset state when user logs in again
    val jobRequestsList = remember(SessionManager.sessionId) { mutableStateListOf<JobRequest>().apply { addAll(initialJobRequests) } }

    val infiniteTransition = rememberInfiniteTransition(label = "requestsGlow")
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
                TopAppBar(
                    title = {
                        Text(
                            text = "Job Requests",
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NeonCyan
                            )
                        }
                    },
                    actions = {
                        // Top-right icon for Pending Works with neon glow on press
                        val iconInteractionSource = remember { MutableInteractionSource() }
                        val isIconPressed by iconInteractionSource.collectIsPressedAsState()
                        val iconScale by animateFloatAsState(
                            targetValue = if (isIconPressed) 0.85f else 1f,
                            animationSpec = tween(100),
                            label = "iconScale"
                        )
                        val iconGlowAlpha by animateFloatAsState(
                            targetValue = if (isIconPressed) 0.8f else 0.3f,
                            animationSpec = tween(150),
                            label = "iconGlow"
                        )
                        
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .scale(iconScale)
                                .shadow(
                                    elevation = (12 * iconGlowAlpha).dp,
                                    shape = CircleShape,
                                    ambientColor = NeonPurple.copy(alpha = iconGlowAlpha * 0.6f),
                                    spotColor = NeonCyan.copy(alpha = iconGlowAlpha * 0.5f)
                                )
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            NeonPurple.copy(alpha = iconGlowAlpha * 0.4f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clickable(
                                    interactionSource = iconInteractionSource,
                                    indication = null,
                                    onClick = { navController.navigate("worker_pending_works") }
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = "Pending Works",
                                tint = NeonCyan.copy(alpha = 0.7f + iconGlowAlpha * 0.3f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NeonBg1.copy(alpha = 0.95f)
                    )
                )
            },
            bottomBar = {
                WorkerBottomNavBar(
                    selectedIndex = selectedNavIndex,
                    onItemSelected = { index: Int ->
                        selectedNavIndex = index
                        when (index) {
                            0 -> navController.navigate("worker_home") {
                                popUpTo("worker_home") { inclusive = true }
                            }
                            1 -> { /* already on requests */ }
                            2 -> navController.navigate("worker_settings")
                        }
                    }
                )
            }
        ) { paddingValues ->
            // Filter out accepted jobs from the visible list
            val pendingRequests = jobRequestsList.filter { !it.isAccepted }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            NeonCyan.copy(alpha = 0.2f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Electrician Jobs",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${pendingRequests.size} new requests",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }

                items(pendingRequests) { request: JobRequest ->
                    JobRequestCard(
                        request = request,
                        onAccept = {
                            // Mark job as accepted
                            val index = jobRequestsList.indexOf(request)
                            if (index != -1) {
                                jobRequestsList[index] = request.copy(isAccepted = true)
                                // Add to PendingWorksState
                                PendingWorksState.addWork(
                                    PendingWorkItem(
                                        clientName = request.clientName,
                                        location = request.location,
                                        workDetails = request.jobDescription,
                                        paymentAmount = request.offeredPrice,
                                        deadlineDate = request.requestedTime
                                    )
                                )
                            }
                        }
                    )
                }

                if (pendingRequests.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "✅",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "All caught up!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "No pending job requests",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// ─── Job Request Card ───────────────────────────────────────────────────────

@Composable
private fun JobRequestCard(request: JobRequest, onAccept: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "cardScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonCyan.copy(alpha = 0.2f),
                spotColor = NeonPurple.copy(alpha = 0.15f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.09f),
                        Color.White.copy(alpha = 0.03f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.4f),
                        NeonPurple.copy(alpha = 0.25f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        NeonPink.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = request.clientName.first().toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPink
                        )
                    }

                    Column {
                        Text(
                            text = request.clientName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = request.location,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.25f),
                                    NeonGreen.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.5f),
                                    NeonGreen.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = request.offeredPrice,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.03f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = request.jobDescription,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = request.requestedTime,
                        fontSize = 12.sp,
                        color = NeonCyan,
                        fontWeight = FontWeight.Medium
                    )
                }

                AcceptButton(onAccept = onAccept)
            }
        }
    }
}

// ─── Accept Button ────────────────────────────────────────────────────────────

@Composable
private fun AcceptButton(onAccept: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonScale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(100),
        label = "btnScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "acceptGlow")
    val pulseGlow: Float by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    Box(
        modifier = Modifier
            .scale(buttonScale)
            .shadow(
                elevation = (16 * pulseGlow).dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeonPink.copy(alpha = pulseGlow * 0.6f),
                spotColor = NeonPurple.copy(alpha = pulseGlow * 0.5f)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(NeonPink, NeonPurple)
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonPink.copy(alpha = 0.9f),
                        NeonPurple.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onAccept
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Accept",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF0F0F1A)
@Composable
fun WorkerRequestsScreenPreview() {
    GigMarketTheme {
        WorkerRequestsScreen(navController = rememberNavController())
    }
}
