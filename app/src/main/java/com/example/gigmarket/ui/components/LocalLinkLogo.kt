package com.example.gigmarket.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigmarket.ui.theme.NeonElectricBlue
import com.example.gigmarket.ui.theme.NeonLinkOrange

/**
 * Reusable LocalLink neon logo composable.
 * "Local" renders in electric blue, "Link" in neon orange,
 * with a smooth neon flicker animation and optional tagline.
 */
@Composable
fun LocalLinkLogo(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 42.sp,
    taglineFontSize: TextUnit = 14.sp,
    showTagline: Boolean = true
) {
    // Smooth neon flicker — very subtle, two-phase animation
    val infiniteTransition = rememberInfiniteTransition(label = "logoFlicker")

    // Phase 1: slow base glow pulse
    val baseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "baseGlow"
    )

    // Phase 2: very occasional micro-flicker (fast, subtle)
    val flicker by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 0.80f,
        animationSpec = infiniteRepeatable(
            animation = tween(120, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flicker"
    )

    // Combined alpha – mostly follows base glow, tiny flicker influence
    val logoAlpha = baseGlow * (0.95f + flicker * 0.05f)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo text row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Local" — electric blue
            Text(
                text = "Local",
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = NeonElectricBlue.copy(alpha = logoAlpha),
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        ambientColor = NeonElectricBlue.copy(alpha = logoAlpha * 0.8f),
                        spotColor = NeonElectricBlue.copy(alpha = logoAlpha * 0.8f)
                    )
            )

            // "Link" — neon orange
            Text(
                text = "Link",
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                color = NeonLinkOrange.copy(alpha = logoAlpha),
                modifier = Modifier
                    .shadow(
                        elevation = 20.dp,
                        ambientColor = NeonLinkOrange.copy(alpha = logoAlpha * 0.8f),
                        spotColor = NeonLinkOrange.copy(alpha = logoAlpha * 0.8f)
                    )
            )
        }

        // Tagline
        if (showTagline) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Connecting Your Community",
                fontSize = taglineFontSize,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.75f),
                letterSpacing = 0.8.sp
            )
        }
    }
}
