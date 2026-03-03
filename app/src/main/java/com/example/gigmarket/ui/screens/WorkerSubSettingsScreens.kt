package com.example.gigmarket.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gigmarket.ui.components.NeonRadialGlowBackground
import com.example.gigmarket.ui.theme.GigMarketTheme
import com.example.gigmarket.ui.theme.NeonBg1
import com.example.gigmarket.ui.theme.NeonBg2
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonLinkOrange
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextPrimary
import com.example.gigmarket.ui.theme.TextSecondary

// Private neon colors
private val NeonPink = Color(0xFFFF69B4)

// ─────────────────────────────────────────────────────────────────────────────
// PROFILE DASHBOARD (Editable)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WorkerProfileScreen(navController: NavController) {
    // Profile state
    var name by remember { mutableStateOf("John Doe") }
    var experience by remember { mutableStateOf("5") }
    var bio by remember { mutableStateOf("Experienced professional offering quality services.") }
    var skills by remember { mutableStateOf(listOf("Plumbing", "Electrical", "Carpentry")) }
    var newSkill by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    
    // Profile photo placeholder index (for UI placeholder change)
    var photoIndex by remember { mutableIntStateOf(0) }
    val photoColors = listOf(NeonCyan, NeonLinkOrange, NeonPurple, NeonGreen)

    val infiniteTransition = rememberInfiniteTransition(label = "profileGlow")
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
                            text = "Profile",
                            color = Color.White,
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
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Profile Photo Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Profile photo placeholder
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            photoColors[photoIndex],
                                            photoColors[photoIndex].copy(alpha = 0.5f)
                                        )
                                    )
                                )
                                .border(
                                    width = 3.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            photoColors[photoIndex].copy(alpha = 0.8f),
                                            photoColors[photoIndex].copy(alpha = 0.3f)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clickable { photoIndex = (photoIndex + 1) % photoColors.size },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.take(2).uppercase(),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Tap to change photo",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Name Field
                ProfileEditableField(
                    label = "Name",
                    value = name,
                    onValueChange = { name = it },
                    isEditing = isEditing
                )

                // Experience Field
                ProfileEditableField(
                    label = "Experience (years)",
                    value = experience,
                    onValueChange = { experience = it },
                    isEditing = isEditing
                )

                // Bio Field
                ProfileEditableField(
                    label = "Bio / About",
                    value = bio,
                    onValueChange = { bio = it },
                    isEditing = isEditing,
                    singleLine = false
                )

                // Skills Section
                Text(
                    text = "Skills / Jobs",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    skills.forEach { skill ->
                        if (isEditing) {
                            InputChip(
                                selected = false,
                                onClick = { skills = skills - skill },
                                label = { Text(skill) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = NeonCyan.copy(alpha = 0.2f),
                                    labelColor = NeonCyan
                                )
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = NeonCyan.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = skill,
                                    color = NeonCyan,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                    
                    // Add new skill button
                    if (isEditing) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                if (newSkill.isNotBlank()) {
                                    skills = skills + newSkill.trim()
                                    newSkill = ""
                                }
                            }
                        ) {
                            InputChip(
                                selected = false,
                                onClick = { },
                                label = { Text("Add") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = NeonGreen.copy(alpha = 0.2f),
                                    labelColor = NeonGreen
                                )
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Quick add skill input
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                androidx.compose.foundation.text.BasicTextField(
                                    value = newSkill,
                                    onValueChange = { newSkill = it },
                                    singleLine = true,
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        color = Color.White,
                                        fontSize = 14.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Edit/Save Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(25.dp),
                            ambientColor = if (isEditing) NeonGreen.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.5f),
                            spotColor = if (isEditing) NeonGreen.copy(alpha = 0.5f) else NeonCyan.copy(alpha = 0.5f)
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    if (isEditing) NeonGreen.copy(alpha = 0.25f) else NeonCyan.copy(alpha = 0.25f),
                                    if (isEditing) NeonGreen.copy(alpha = 0.35f) else NeonCyan.copy(alpha = 0.35f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    if (isEditing) NeonGreen.copy(alpha = 0.8f) else NeonCyan.copy(alpha = 0.8f),
                                    if (isEditing) NeonGreen.copy(alpha = 0.4f) else NeonCyan.copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable {
                            isEditing = !isEditing
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEditing) "Save Changes" else "Edit Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEditing) NeonGreen else NeonCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileEditableField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean,
    singleLine: Boolean = true
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isEditing) NeonCyan.copy(alpha = 0.4f) else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            if (isEditing) {
                androidx.compose.foundation.text.BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = singleLine,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontSize = 15.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = value,
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PRIVACY DASHBOARD (Editable)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerPrivacyScreen(navController: NavController) {
    // Privacy state
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var isEmailVerified by remember { mutableStateOf(true) }
    var showOnlineStatus by remember { mutableStateOf(true) }
    var showLocation by remember { mutableStateOf(false) }
    var allowMessages by remember { mutableStateOf(true) }

    val infiniteTransition = rememberInfiniteTransition(label = "privacyGlow")
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
                            text = "Privacy",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NeonPurple
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NeonBg1.copy(alpha = 0.95f)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Change Password Section
                Text(
                    text = "Change Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPurple
                )

                // Current Password
                PrivacyTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = "Current Password"
                )

                // New Password
                PrivacyTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password"
                )

                // Confirm Password
                PrivacyTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm New Password"
                )

                // Verification Section
                Text(
                    text = "Verification",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPurple,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Phone Verification
                VerificationRow(
                    label = "Verify Phone Number",
                    isVerified = isPhoneVerified,
                    onToggle = { isPhoneVerified = !isPhoneVerified },
                    accentColor = NeonGreen
                )

                // Email Verification
                VerificationRow(
                    label = "Verify Email Address",
                    isVerified = isEmailVerified,
                    onToggle = { isEmailVerified = !isEmailVerified },
                    accentColor = NeonGreen
                )

                // Privacy Settings Section
                Text(
                    text = "Privacy Settings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPurple,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Show Online Status
                PrivacySwitchRow(
                    label = "Show Online Status",
                    description = "Let others see when you're online",
                    isEnabled = showOnlineStatus,
                    onToggle = { showOnlineStatus = !showOnlineStatus }
                )

                // Show Location
                PrivacySwitchRow(
                    label = "Show Location",
                    description = "Share your location with customers",
                    isEnabled = showLocation,
                    onToggle = { showLocation = !showLocation }
                )

                // Allow Messages
                PrivacySwitchRow(
                    label = "Allow Messages",
                    description = "Receive messages from customers",
                    isEnabled = allowMessages,
                    onToggle = { allowMessages = !allowMessages }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(25.dp),
                            ambientColor = NeonPurple.copy(alpha = 0.5f),
                            spotColor = NeonPurple.copy(alpha = 0.5f)
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonPurple.copy(alpha = 0.25f),
                                    NeonPurple.copy(alpha = 0.35f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonPurple.copy(alpha = 0.8f),
                                    NeonPurple.copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { /* UI only */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPurple
                    )
                }
            }
        }
    }
}

@Composable
private fun PrivacyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 15.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun VerificationRow(
    label: String,
    isVerified: Boolean,
    onToggle: () -> Unit,
    accentColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = TextPrimary
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isVerified) "Verified" else "Not Verified",
                fontSize = 14.sp,
                color = if (isVerified) accentColor else TextSecondary
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            if (!isVerified) {
                TextButton(onClick = onToggle) {
                    Text(
                        text = "Verify",
                        color = accentColor,
                        fontSize = 14.sp
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Verified",
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacySwitchRow(
    label: String,
    description: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 15.sp,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
        
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonCyan,
                checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PERSONALIZATION DASHBOARD (Editable)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerPersonalizationScreen(navController: NavController) {
    // Personalization state
    var selectedTheme by remember { mutableStateOf("Neon") }
    var selectedFontSize by remember { mutableStateOf("Medium") }
    var enableAnimations by remember { mutableStateOf(true) }
    var enableHaptics by remember { mutableStateOf(true) }
    var compactMode by remember { mutableStateOf(false) }

    val themes = listOf("Dark", "Light", "Neon")
    val fontSizes = listOf("Small", "Medium", "Large")

    val infiniteTransition = rememberInfiniteTransition(label = "personalizeGlow")
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
                            text = "Personalization",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NeonPink
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NeonBg1.copy(alpha = 0.95f)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // App Theme Section
                Text(
                    text = "App Theme",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPink
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    themes.forEach { theme ->
                        SelectionChip(
                            text = theme,
                            isSelected = selectedTheme == theme,
                            onClick = { selectedTheme = theme },
                            accentColor = when (theme) {
                                "Dark" -> Color(0xFF2D2D2D)
                                "Light" -> Color(0xFFE0E0E0)
                                else -> NeonCyan
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Font Size Section
                Text(
                    text = "Font Size",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPink
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    fontSizes.forEach { size ->
                        SelectionChip(
                            text = size,
                            isSelected = selectedFontSize == size,
                            onClick = { selectedFontSize = size },
                            accentColor = NeonPink,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Preview Text
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Preview Text",
                        fontSize = when (selectedFontSize) {
                            "Small" -> 12.sp
                            "Medium" -> 16.sp
                            else -> 20.sp
                        },
                        color = when (selectedTheme) {
                            "Dark" -> Color.White
                            "Light" -> Color.Black
                            else -> NeonCyan
                        },
                        fontWeight = FontWeight.Medium
                    )
                }

                // UI Preferences Section
                Text(
                    text = "UI Preferences",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NeonPink,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Enable Animations
                PersonalizationSwitchRow(
                    label = "Enable Animations",
                    description = "Smooth transitions and effects",
                    isEnabled = enableAnimations,
                    onToggle = { enableAnimations = !enableAnimations }
                )

                // Enable Haptics
                PersonalizationSwitchRow(
                    label = "Enable Haptics",
                    description = "Vibration feedback",
                    isEnabled = enableHaptics,
                    onToggle = { enableHaptics = !enableHaptics }
                )

                // Compact Mode
                PersonalizationSwitchRow(
                    label = "Compact Mode",
                    description = "Denser UI layout",
                    isEnabled = compactMode,
                    onToggle = { compactMode = !compactMode }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(25.dp),
                            ambientColor = NeonPink.copy(alpha = 0.5f),
                            spotColor = NeonPink.copy(alpha = 0.5f)
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonPink.copy(alpha = 0.25f),
                                    NeonPink.copy(alpha = 0.35f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonPink.copy(alpha = 0.8f),
                                    NeonPink.copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { /* UI only */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save Preferences",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonPink
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectionChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .shadow(
                elevation = if (isSelected) 12.dp else 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = accentColor.copy(alpha = if (isSelected) 0.4f else 0.1f),
                spotColor = accentColor.copy(alpha = if (isSelected) 0.4f else 0.1f)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isSelected) {
                        listOf(accentColor.copy(alpha = 0.25f), accentColor.copy(alpha = 0.15f))
                    } else {
                        listOf(Color.White.copy(alpha = 0.08f), Color.White.copy(alpha = 0.03f))
                    }
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = if (isSelected) 1.5.dp else 0.5.dp,
                color = if (isSelected) accentColor.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) accentColor else TextSecondary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonalizationSwitchRow(
    label: String,
    description: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 15.sp,
                color = TextPrimary
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
        
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonPink,
                checkedTrackColor = NeonPink.copy(alpha = 0.3f),
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CHECK FOR UPDATE DASHBOARD
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerUpdateScreen(navController: NavController) {
    val currentVersion = "1.0.0"

    val infiniteTransition = rememberInfiniteTransition(label = "updateGlow")
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
                            text = "Check for Updates",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = NeonGreen
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = NeonBg1.copy(alpha = 0.95f)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // App Icon Placeholder
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(
                            elevation = 30.dp,
                            shape = RoundedCornerShape(30.dp),
                            ambientColor = NeonGreen.copy(alpha = glowAlpha * 0.5f),
                            spotColor = NeonGreen.copy(alpha = glowAlpha * 0.5f)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.3f),
                                    NeonGreen.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.6f),
                                    NeonGreen.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔧",
                        fontSize = 48.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // App Name
                Text(
                    text = "GigMarket",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Version Text
                Text(
                    text = "Version $currentVersion",
                    fontSize = 16.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Status Message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = NeonGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "You are on the latest version.",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = NeonGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Check for Updates Button (UI only)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(25.dp),
                            ambientColor = NeonGreen.copy(alpha = 0.3f),
                            spotColor = NeonGreen.copy(alpha = 0.3f)
                        )
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.15f),
                                    NeonGreen.copy(alpha = 0.25f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = 0.4f),
                                    NeonGreen.copy(alpha = 0.2f)
                                )
                            ),
                            shape = RoundedCornerShape(25.dp)
                        )
                        .clickable { /* UI only - no real update check */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Check for Updates",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NeonGreen
                    )
                }
            }
        }
    }
}
