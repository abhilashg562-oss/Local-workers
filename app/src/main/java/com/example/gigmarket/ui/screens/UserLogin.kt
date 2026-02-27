package com.example.gigmarket.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.network.Provider
import com.example.gigmarket.ui.components.LocalLinkLogo
import com.example.gigmarket.ui.components.OsmMapView
import com.example.gigmarket.ui.theme.CardBackground
import com.example.gigmarket.ui.theme.CardBorder
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonOrange
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary
import com.example.gigmarket.viewmodel.AppLanguage
import com.example.gigmarket.viewmodel.UserLoginViewModel
import com.example.gigmarket.viewmodel.WorkerWithDistance

@Composable
fun UserLogin(
    navController: NavController,
    languageViewModel: com.example.gigmarket.viewmodel.LanguageViewModel = viewModel()
) {
    val context = LocalContext.current
    val viewModel: UserLoginViewModel = viewModel()

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredWorkers by viewModel.filteredWorkers.collectAsState()
    val userLat by viewModel.userLat.collectAsState()
    val userLng by viewModel.userLng.collectAsState()

    var categoriesExpanded by remember { mutableStateOf(false) }
    var hasLocationPermission by remember { mutableStateOf(false) }
    var isLocationEnabled by remember { mutableStateOf(false) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (hasLocationPermission) {
            isLocationEnabled = true
        }
    }

    // Check permission on launch
    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Default location (Bangalore) if no permission
    val userLatitude = if (isLocationEnabled && userLat != null) userLat!! else 12.97
    val userLongitude = if (isLocationEnabled && userLng != null) userLng!! else 77.59

    // Static categories list
    val categories = listOf(
        "Plumber",
        "Electrician",
        "Carpenter",
        "Cleaner",
        "Painter",
        "Mechanic"
    )

    // Rotation animation for dropdown arrow
    val rotationAngle by animateFloatAsState(
        targetValue = if (categoriesExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "arrow_rotation"
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
                .widthIn(max = 430.dp)
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 16.dp)
        ) {
            // Header Row with Back Button, Title, and Location Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back Button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonCyan
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Map View",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                // Location Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = if (isLocationEnabled) NeonCyan.copy(alpha = 0.15f) else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Location",
                        tint = if (isLocationEnabled) NeonCyan else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = isLocationEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled && !hasLocationPermission) {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            } else {
                                isLocationEnabled = enabled
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                            uncheckedThumbColor = TextSecondary,
                            uncheckedTrackColor = TextSecondary.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            // LocalLink Logo at top (smaller)
            LocalLinkLogo(
                fontSize = 28.sp,
                taglineFontSize = 10.sp,
                showTagline = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Categories Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = NeonCyan.copy(alpha = 0.1f),
                        spotColor = NeonPurple.copy(alpha = 0.1f)
                    ),
                shape = RoundedCornerShape(16.dp),
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
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column {
                        // Categories Header - Clickable
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { categoriesExpanded = !categoriesExpanded }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Categories",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expand",
                                tint = NeonCyan,
                                modifier = Modifier
                                    .size(24.dp)
                                    .rotate(rotationAngle)
                            )
                        }

                        // Dropdown Categories List
                        AnimatedVisibility(
                            visible = categoriesExpanded,
                            enter = expandVertically(animationSpec = tween(300)) + fadeIn(),
                            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 12.dp)
                            ) {
                                categories.forEach { category ->
                                    val isSelected = selectedCategory == category
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(
                                                color = if (isSelected) NeonCyan.copy(alpha = 0.15f) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .border(
                                                width = if (isSelected) 1.dp else 0.dp,
                                                color = if (isSelected) NeonCyan.copy(alpha = 0.5f) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                viewModel.onCategorySelected(category, userLatitude, userLongitude)
                                                categoriesExpanded = false
                                            }
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = category,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) NeonCyan else TextSecondary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Map Section - Show when category is selected
            AnimatedVisibility(
                visible = selectedCategory != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    // Map
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .shadow(
                                elevation = 15.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = NeonCyan.copy(alpha = 0.1f),
                                spotColor = NeonPurple.copy(alpha = 0.1f)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
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
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            if (userLat != null || hasLocationPermission) {
                                OsmMapView(
                                    userLat = userLatitude,
                                    userLng = userLongitude,
                                    workers = filteredWorkers,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(16.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        CircularProgressIndicator(
                                            color = NeonCyan,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Loading map...",
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Worker List Section
                    Text(
                        text = "Nearby ${selectedCategory}s",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    if (filteredWorkers.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "😔",
                                    fontSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No workers found nearby",
                                    color = TextSecondary
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 60.dp)
                        ) {
                            items(filteredWorkers) { workerWithDistance ->
                            WorkerCard(
                                    workerWithDistance = workerWithDistance,
                                    onClick = {
                                        // Navigate to Worker Profile Screen with all worker data
                                        val provider = workerWithDistance.provider
                                        navController.navigate(
                                            "/worker-profile/${provider._id}/${provider.name}/${provider.phone}/${provider.category}/${provider.rating}/${provider.totalReviews}/${provider.bio ?: ""}/${(provider.skills).joinToString("|||")}/${provider.languages.joinToString("|||")}/${provider.hourlyRate ?: "0"}/${provider.experience ?: ""}/${provider.isVerified}/${provider.isAvailable}/${provider.location.address ?: ""}/${provider.location.coordinates.getOrNull(1) ?: 0.0}/${provider.location.coordinates.getOrNull(0) ?: 0.0}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // If no category selected, show instruction
            if (selectedCategory == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Select a category to find workers nearby",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun WorkerCard(
    workerWithDistance: WorkerWithDistance,
    onClick: () -> Unit
) {
    val provider = workerWithDistance.provider

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeonPurple.copy(alpha = 0.1f),
                spotColor = NeonCyan.copy(alpha = 0.1f)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
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
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = DarkBackground
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = provider.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = NeonOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${provider.rating}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Text(
                                text = " (${provider.totalReviews})",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = provider.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = NeonCyan
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Distance",
                            tint = NeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = String.format("%.1f km", workerWithDistance.distanceKm),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = NeonGreen
                        )
                    }

                    provider.hourlyRate?.let { rate ->
                        Text(
                            text = "₹${rate.toInt()}/hr",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

// Localization data class and function for UserDashboard
data class LocalizedStrings(
    val userLogin: String,
    val fullName: String,
    val phoneNumber: String,
    val enterOtp: String,
    val verifyOtp: String,
    val goBack: String,
    val enterValidOtp: String,
    val pleaseFillAllFields: String
)

fun getLocalizedStrings(context: Context, language: AppLanguage): LocalizedStrings {
    return when (language) {
        AppLanguage.ENGLISH -> LocalizedStrings(
            userLogin = "User Login",
            fullName = "Full Name",
            phoneNumber = "Phone Number",
            enterOtp = "Enter 6-digit OTP",
            verifyOtp = "Verify OTP",
            goBack = "Go Back",
            enterValidOtp = "Please enter a valid 6-digit OTP",
            pleaseFillAllFields = "Please fill in all fields"
        )
        AppLanguage.HINDI -> LocalizedStrings(
            userLogin = "उपयोगकर्ता लॉगिन",
            fullName = "पूरा नाम",
            phoneNumber = "फोन नंबर",
            enterOtp = "6 अंकों का OTP दर्ज करें",
            verifyOtp = "OTP सत्यापित करें",
            goBack = "वापस जाएं",
            enterValidOtp = "वैध 6 अंकों का OTP दर्ज करें",
            pleaseFillAllFields = "कृपया सभी फ़ील्ड भरें"
        )
        AppLanguage.KANNADA -> LocalizedStrings(
            userLogin = "ಬಳಕೆದಾರ ಲಾಗಿನ್",
            fullName = "ಪೂರ್ಣ ಹೆಸರು",
            phoneNumber = "ಫೋನ್ ಸಂಖ್ಯೆ",
            enterOtp = "6 ಅಂಕಿಗಳ OTP ನಮೂದಿಸಿ",
            verifyOtp = "OTP ಪರಿಶೀಲಿಸಿ",
            goBack = "ಹಿಂದೆ ಹೋಗಿ",
            enterValidOtp = "ಮಾನ್ಯವಾದ 6 ಅಂಕಿಗಳ OTP ನಮೂದಿಸಿ",
            pleaseFillAllFields = "ದಯವಿಟ್ಟು ಎಲ್ಲಾ ಫೀಲ್ಡ್‌ಗಳನ್ನು ತುಂಬಿರಿ"
        )
    }
}

