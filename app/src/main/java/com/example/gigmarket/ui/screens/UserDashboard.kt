package com.example.gigmarket.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.network.Provider
import com.example.gigmarket.network.WorkingHours
import com.example.gigmarket.ui.theme.CardBackground
import com.example.gigmarket.ui.theme.CardBorder
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonOrange
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary
import com.example.gigmarket.viewmodel.ProviderViewModel

enum class DashboardState {
    SERVICE_SELECTION,
    PROVIDER_LIST,
    PROVIDER_DETAILS
}

@Composable
fun UserDashboard(
    navController: NavController,
    userName: String = "",
    viewModel: ProviderViewModel = viewModel()
) {
    val providers by viewModel.providers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()

    var dashboardState by remember { mutableStateOf(DashboardState.SERVICE_SELECTION) }
    var selectedProvider by remember { mutableStateOf<Provider?>(null) }

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
                .padding(16.dp)
        ) {
            // Header with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (dashboardState != DashboardState.SERVICE_SELECTION) {
                    IconButton(
                        onClick = {
                            when (dashboardState) {
                                DashboardState.PROVIDER_DETAILS -> {
                                    dashboardState = DashboardState.PROVIDER_LIST
                                    selectedProvider = null
                                }
                                DashboardState.PROVIDER_LIST -> {
                                    dashboardState = DashboardState.SERVICE_SELECTION
                                    viewModel.clearSelection()
                                }
                                else -> {}
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = when {
                            dashboardState == DashboardState.SERVICE_SELECTION && userName.isNotEmpty() -> "Hi, $userName! 👋"
                            dashboardState == DashboardState.SERVICE_SELECTION -> "Welcome! 👋"
                            dashboardState == DashboardState.PROVIDER_DETAILS && selectedProvider != null -> selectedProvider!!.name
                            else -> "Available Workers"
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = when {
                            dashboardState == DashboardState.PROVIDER_DETAILS -> "Worker Details"
                            dashboardState == DashboardState.PROVIDER_LIST && selectedService != null -> "Nearby $selectedService"
                            else -> "What service do you need?"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }

                if (dashboardState == DashboardState.SERVICE_SELECTION) {
                    // Location indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(
                                color = NeonCyan.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "5km",
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Content based on state
            AnimatedVisibility(
                visible = dashboardState == DashboardState.SERVICE_SELECTION,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                // Service Categories Grid
                ServiceCategoryGrid(
                    onServiceClick = { service ->
                        viewModel.selectService(service)
                        dashboardState = DashboardState.PROVIDER_LIST
                    }
                )
            }

            AnimatedVisibility(
                visible = dashboardState == DashboardState.PROVIDER_LIST,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                // Providers List
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = NeonCyan,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Finding workers nearby...",
                                color = TextSecondary
                            )
                        }
                    }
                } else if (providers.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "😔",
                                fontSize = 64.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No workers found nearby",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                text = "Try selecting a different service",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    dashboardState = DashboardState.SERVICE_SELECTION
                                    viewModel.clearSelection()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonCyan.copy(alpha = 0.2f)
                                )
                            ) {
                                Text(
                                    text = "Browse Other Services",
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 60.dp)
                    ) {
                        items(providers) { provider ->
                            ProviderCard(
                                provider = provider,
                                onClick = {
                                    selectedProvider = provider
                                    dashboardState = DashboardState.PROVIDER_DETAILS
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = dashboardState == DashboardState.PROVIDER_DETAILS && selectedProvider != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                selectedProvider?.let { provider ->
                    ProviderDetailsScreen(provider = provider)
                }
            }
        }

        // Logout button at bottom
        if (dashboardState == DashboardState.SERVICE_SELECTION) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "← Logout",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    modifier = Modifier
                        .clickable {
                            viewModel.clearSelection()
                            navController.navigate("/") {
                                popUpTo("/") { inclusive = true }
                            }
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun ProviderDetailsScreen(provider: Provider) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Profile Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 25.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = NeonPurple.copy(alpha = 0.2f),
                    spotColor = NeonCyan.copy(alpha = 0.2f)
                ),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(CardBackground, DarkBackgroundSecondary.copy(alpha = 0.6f))
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.3f), NeonPurple.copy(alpha = 0.1f))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar with gradient
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(NeonCyan, NeonPurple)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = provider.name.first().toString(),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = DarkBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name with verification
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = provider.name,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        if (provider.isVerified) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified",
                                tint = NeonCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = NeonOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${provider.rating}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        Text(
                            text = " (${provider.totalReviews} reviews)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Availability badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (provider.isAvailable) NeonGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (provider.isAvailable) "● Available Now" else "● Not Available",
                            color = if (provider.isAvailable) NeonGreen else Color.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Call Button
            Button(
                onClick = { /* Make call */ },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = DarkBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Call Now",
                    color = DarkBackground,
                    fontWeight = FontWeight.Bold
                )
            }

            // Book Button
            Button(
                onClick = { /* Book service */ },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Book",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Book",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Details Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = NeonPurple.copy(alpha = 0.1f),
                    spotColor = NeonCyan.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(CardBackground, DarkBackgroundSecondary.copy(alpha = 0.5f))
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(CardBorder, CardBorder.copy(alpha = 0.3f))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(20.dp)
            ) {
                Column {
                    // About Section
                    DetailSection(
                        icon = Icons.Default.Description,
                        title = "About",
                        content = provider.bio ?: "No description available"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Phone Number
                    DetailSection(
                        icon = Icons.Default.Call,
                        title = "Phone Number",
                        content = provider.phone
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Experience
                    DetailSection(
                        icon = Icons.Default.WorkHistory,
                        title = "Experience",
                        content = provider.experience ?: "Not specified"
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Languages
                    DetailSection(
                        icon = Icons.Default.Language,
                        title = "Languages",
                        content = provider.languages.joinToString(", ")
                    )

                    // Working Hours
                    provider.workingHours?.let { hours ->
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = CardBorder
                        )
                        DetailSection(
                            icon = Icons.Default.AccessTime,
                            title = "Working Hours",
                            content = "",
                            customContent = {
                                WorkingHoursDisplay(hours)
                            }
                        )
                    }

                    // Certifications
                    provider.certifications?.let { certs ->
                        if (certs.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = CardBorder
                            )
                            DetailSection(
                                icon = Icons.Default.Badge,
                                title = "Certifications",
                                content = "",
                                customContent = {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        certs.forEach { cert ->
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = NeonCyan.copy(alpha = 0.15f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = cert,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = NeonCyan
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // Skills
                    if (provider.skills.isNotEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = CardBorder
                        )
                        Text(
                            text = "Skills & Services",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            provider.skills.forEach { skill ->
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = NeonPurple.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = skill,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = NeonPurple
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Location
                    DetailSection(
                        icon = Icons.Default.LocationOn,
                        title = "Service Area",
                        content = provider.location.address ?: "Location not specified"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun DetailSection(
    icon: ImageVector,
    title: String,
    content: String,
    customContent: @Composable (() -> Unit)? = null
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeonCyan,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (customContent != null) {
            customContent()
        } else {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
fun WorkingHoursDisplay(hours: WorkingHours) {
    Column {
        WorkingHoursRow("Monday", hours.monday)
        WorkingHoursRow("Tuesday", hours.tuesday)
        WorkingHoursRow("Wednesday", hours.wednesday)
        WorkingHoursRow("Thursday", hours.thursday)
        WorkingHoursRow("Friday", hours.friday)
        WorkingHoursRow("Saturday", hours.saturday)
        WorkingHoursRow("Sunday", hours.sunday)
    }
}

@Composable
fun WorkingHoursRow(day: String, hours: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Text(
            text = hours,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (hours == "Closed") Color.Red else NeonGreen
        )
    }
}

@Composable
fun ServiceCategoryGrid(
    onServiceClick: (String) -> Unit
) {
    val categories = ServiceCategories.categories

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(categories) { service ->
            ServiceCategoryCard(
                service = service,
                onClick = { onServiceClick(service.name) }
            )
        }
    }
}

@Composable
fun ServiceCategoryCard(
    service: ServiceCategories.Service,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonCyan.copy(alpha = 0.1f),
                spotColor = NeonPurple.copy(alpha = 0.1f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CardBackground,
                            DarkBackgroundSecondary.copy(alpha = 0.5f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CardBorder,
                            CardBorder.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon with neon glow
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = service.icon,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = service.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun ProviderCard(provider: Provider, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = NeonPurple.copy(alpha = 0.15f),
                spotColor = NeonCyan.copy(alpha = 0.15f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CardBackground,
                            DarkBackgroundSecondary.copy(alpha = 0.6f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.3f),
                            NeonPurple.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Profile section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Profile avatar
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(NeonCyan, NeonPurple)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = provider.name.first().toString(),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = DarkBackground
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = provider.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )

                                if (provider.isVerified) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "✓",
                                        color = NeonCyan,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            // Rating
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = NeonOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${provider.rating}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = " (${provider.totalReviews} reviews)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    // Hourly rate
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "₹${provider.hourlyRate?.toInt() ?: "--"}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = NeonCyan
                        )
                        Text(
                            text = "/hour",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Skills
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    provider.skills.take(3).forEach { skill ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = NeonPurple.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = skill,
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonPurple
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Location and Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = provider.location.address ?: "Nearby",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Contact button
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Call button
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(NeonCyan, NeonPurple)
                                    )
                                )
                                .clickable { /* Call action */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Call",
                                tint = DarkBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

