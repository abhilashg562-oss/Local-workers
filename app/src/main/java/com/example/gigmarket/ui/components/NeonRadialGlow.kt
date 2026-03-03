package com.example.gigmarket.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonPink
import com.example.gigmarket.ui.theme.NeonPurple

// ─── Animated Radial Glow Background ──────────────────────────────────────────

/**
 * Reusable neon radial glow background component with 8-10 second pulse animation.
 * Creates a premium futuristic neon effect with subtle animated radial glows.
 */
@Composable
fun NeonRadialGlowBackground(
    glowAlpha: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Top-left cyan glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonCyan.copy(alpha = glowAlpha),
                    Color.Transparent
                ),
                center = Offset(w * 0.15f, h * 0.25f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.15f, h * 0.25f)
        )

        // Bottom-right purple glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonPurple.copy(alpha = glowAlpha * 0.85f),
                    Color.Transparent
                ),
                center = Offset(w * 0.85f, h * 0.78f),
                radius = w * 0.55f
            ),
            radius = w * 0.55f,
            center = Offset(w * 0.85f, h * 0.78f)
        )

        // Center subtle pink accent
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    NeonPink.copy(alpha = glowAlpha * 0.4f),
                    Color.Transparent
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.4f
            ),
            radius = w * 0.4f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}

/**
 * Helper function to create the infinite transition animation state for glow effect.
 * Uses 9 second pulse loop as specified in the design system.
 */
@Composable
fun rememberNeonGlowState(): Float {
    val infiniteTransition = rememberInfiniteTransition(label = "neonGlow")
    return infiniteTransition.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.20f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    ).value
}
