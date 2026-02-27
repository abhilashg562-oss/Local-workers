package com.example.gigmarket

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gigmarket.ui.components.LocalLinkLogo
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.NeonElectricBlue
import com.example.gigmarket.ui.theme.NeonLinkOrange

@Composable
fun LoginPlaceholder(navController: NavController, role: String) {
    val accentColor = if (role == "Customer") NeonElectricBlue else NeonLinkOrange

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocalLinkLogo(
                fontSize = 38.sp,
                taglineFontSize = 13.sp,
                showTagline = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "$role Login",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Coming Soon...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "← Go back to home",
                style = MaterialTheme.typography.labelLarge,
                color = accentColor,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { navController.popBackStack() }
            )
        }
    }
}

