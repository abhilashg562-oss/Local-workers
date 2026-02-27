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
import androidx.compose.material.icons.filled.Star
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
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextPrimary
import com.example.gigmarket.ui.theme.TextSecondary

// Define NeonRed locally since it's not in the theme
private val NeonRed = Color(0xFFFF4444)

// ─── Data Models ─────────────────────────────────────────────────────────────

private data class Review(
    val customerName: String,
    val rating: Int,
    val reviewText: String,
    val date: String,
    val isNegative: Boolean = false
)

private val reviews: List<Review> = listOf(
    Review(
        customerName = "Rahul Sharma",
        rating = 5,
        reviewText = "Excellent work! Very professional and completed the job on time. Would definitely recommend to others.",
        date = "2 days ago"
    ),
    Review(
        customerName = "Priya Menon",
        rating = 4,
        reviewText = "Good service, arrived on time. Minor delay in completing but overall satisfied with the work.",
        date = "1 week ago"
    ),
    Review(
        customerName = "Arjun Patel",
        rating = 2,
        reviewText = "Very disappointed with the service. The work was not completed properly and had to call another technician. Not recommended.",
        date = "2 weeks ago",
        isNegative = true
    ),
    Review(
        customerName = "Sneha Rao",
        rating = 5,
        reviewText = "Amazing experience! The technician was very knowledgeable and explained everything clearly. Will hire again.",
        date = "3 weeks ago"
    ),
    Review(
        customerName = "Vikram Singh",
        rating = 4,
        reviewText = "Great job on the electrical work. Fair pricing and quality service. Could be a bit more punctual.",
        date = "1 month ago"
    )
)

// ─── Main Review History Screen ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerReviewHistoryScreen(navController: NavController) {
    var selectedNavIndex by remember { mutableIntStateOf(2) }

    val infiniteTransition = rememberInfiniteTransition(label = "reviewGlow")
    val glowAlpha: Float by infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val averageRating: Double = reviews.map { it.rating }.average()
    val totalReviews: Int = reviews.size

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
                            text = "Review History",
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
                            1 -> navController.navigate("worker_requests")
                            2 -> { /* already on settings */ }
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item {
                    ReviewSummaryHeader(
                        averageRating = averageRating,
                        totalReviews = totalReviews
                    )
                }

                item {
                    Text(
                        text = "All Reviews",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }

                items(reviews) { review: Review ->
                    ReviewCard(review = review)
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

// ─── Review Summary Header ──────────────────────────────────────────────────

@Composable
private fun ReviewSummaryHeader(
    averageRating: Double,
    totalReviews: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonCyan.copy(alpha = 0.25f),
                spotColor = NeonPurple.copy(alpha = 0.2f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.1f),
                        Color.White.copy(alpha = 0.04f)
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
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = String.format("%.1f", averageRating),
                    fontSize = 48.sp,
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
                text = "Based on $totalReviews reviews",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

// ─── Review Card ────────────────────────────────────────────────────────────

@Composable
private fun ReviewCard(review: Review) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardScale: Float by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = tween(100),
        label = "cardScale"
    )

    val glowColor: Color = if (review.isNegative) NeonRed else NeonCyan

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale)
            .shadow(
                elevation = if (review.isNegative) 20.dp else 14.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = glowColor.copy(alpha = if (review.isNegative) 0.3f else 0.15f),
                spotColor = glowColor.copy(alpha = if (review.isNegative) 0.25f else 0.1f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = if (review.isNegative) 0.08f else 0.06f),
                        Color.White.copy(alpha = if (review.isNegative) 0.03f else 0.02f)
                    )
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                brush = if (review.isNegative) {
                    Brush.linearGradient(
                        colors = listOf(
                            NeonRed.copy(alpha = 0.5f),
                            NeonRed.copy(alpha = 0.3f)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.35f),
                            NeonPurple.copy(alpha = 0.2f)
                        )
                    )
                },
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
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
                                        if (review.isNegative) NeonRed.copy(alpha = 0.25f)
                                        else NeonCyan.copy(alpha = 0.2f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.customerName.first().toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (review.isNegative) NeonRed else NeonCyan
                        )
                    }

                    Column {
                        Text(
                            text = review.customerName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            text = review.date,
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (review.isNegative) NeonRed else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    repeat(5 - review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = TextSecondary.copy(alpha = 0.3f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
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
                    text = review.reviewText,
                    fontSize = 13.sp,
                    color = if (review.isNegative) TextSecondary.copy(alpha = 0.9f) else TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

// ─── Preview ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFF0F0F1A)
@Composable
fun WorkerReviewHistoryScreenPreview() {
    GigMarketTheme {
        WorkerReviewHistoryScreen(navController = rememberNavController())
    }
}
