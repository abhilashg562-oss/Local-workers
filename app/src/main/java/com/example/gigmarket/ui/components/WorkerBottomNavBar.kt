package com.example.gigmarket.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gigmarket.ui.theme.NeonBg1
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary

// ─── Worker Bottom Navigation Bar ─────────────────────────────────────────────

@Composable
fun WorkerBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val navItems: List<Triple<ImageVector, String, String>> = listOf(
        Triple(Icons.Default.Home, "Home", "home"),
        Triple(Icons.Default.Work, "Requests", "requests"),
        Triple(Icons.Default.Settings, "Settings", "settings")
    )

    Column(modifier = modifier) {
        HorizontalDivider(
            color = NeonCyan.copy(alpha = 0.18f),
            thickness = 1.dp
        )
        NavigationBar(
            containerColor = NeonBg1.copy(alpha = 0.97f),
            tonalElevation = 0.dp
        ) {
            navItems.forEachIndexed { index: Int, item: Triple<ImageVector, String, String> ->
                val (icon, label, _) = item
                val isSelected = selectedIndex == index

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onItemSelected(index) },
                    icon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            modifier = if (isSelected) {
                                Modifier.shadow(
                                    elevation = 16.dp,
                                    shape = CircleShape,
                                    ambientColor = NeonCyan.copy(alpha = 0.7f),
                                    spotColor = NeonPurple.copy(alpha = 0.5f)
                                )
                            } else Modifier
                        )
                    },
                    label = {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary,
                        indicatorColor = NeonCyan.copy(alpha = 0.12f)
                    )
                )
            }
        }
    }
}
