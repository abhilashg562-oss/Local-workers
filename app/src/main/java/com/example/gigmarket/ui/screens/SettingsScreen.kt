package com.example.gigmarket.ui.screens

import android.app.AlertDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.ui.theme.CardBackground
import com.example.gigmarket.ui.theme.CardBorder
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.LightBackground
import com.example.gigmarket.ui.theme.LightBackgroundSecondary
import com.example.gigmarket.ui.theme.LightCardBackground
import com.example.gigmarket.ui.theme.LightCardBorder
import com.example.gigmarket.ui.theme.LightTextPrimary
import com.example.gigmarket.ui.theme.LightTextSecondary
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary
import com.example.gigmarket.viewmodel.AppLanguage
import com.example.gigmarket.viewmodel.LanguageViewModel
import com.example.gigmarket.viewmodel.ThemeViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    languageViewModel: LanguageViewModel = viewModel(),
    themeViewModel: ThemeViewModel = viewModel()
) {
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    var showLanguageMenu by remember { mutableStateOf(false) }
    
    // Verification states (mock - would be from backend in real app)
    var isEmailVerified by remember { mutableStateOf(false) }
    var isPhoneVerified by remember { mutableStateOf(false) }
    
    val localizedStrings = getSettingsStrings(currentLanguage)
    
    // Choose colors based on theme
    val backgroundColor = if (isDarkTheme) DarkBackground else LightBackground
    val backgroundSecondary = if (isDarkTheme) DarkBackgroundSecondary else LightBackgroundSecondary
    val cardBg = if (isDarkTheme) CardBackground else LightCardBackground
    val cardBorder = if (isDarkTheme) CardBorder else LightCardBorder
    val textPrimary = if (isDarkTheme) Color.White else LightTextPrimary
    val textSecondary = if (isDarkTheme) TextSecondary else LightTextSecondary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(backgroundColor, backgroundSecondary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
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

                Text(
                    text = localizedStrings.settings,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = textPrimary
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Language Selector
                Box {
                    Box(
                        modifier = Modifier
                            .clickable { showLanguageMenu = true }
                            .background(
                                color = NeonCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (currentLanguage) {
                                    AppLanguage.ENGLISH -> "EN"
                                    AppLanguage.HINDI -> "HI"
                                    AppLanguage.KANNADA -> "KN"
                                },
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "▼",
                                color = NeonCyan,
                                fontSize = 10.sp
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showLanguageMenu,
                        onDismissRequest = { showLanguageMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("English") },
                            onClick = {
                                languageViewModel.setLanguage(AppLanguage.ENGLISH)
                                showLanguageMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("हिन्दी") },
                            onClick = {
                                languageViewModel.setLanguage(AppLanguage.HINDI)
                                showLanguageMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ಕನ್ನಡ") },
                            onClick = {
                                languageViewModel.setLanguage(AppLanguage.KANNADA)
                                showLanguageMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 1. Email Verification Card
            SettingsCard(
                icon = Icons.Default.Email,
                title = localizedStrings.emailVerification,
                subtitle = if (isEmailVerified) localizedStrings.verified else localizedStrings.notVerified,
                isVerified = isEmailVerified,
                showButton = !isEmailVerified,
                buttonText = localizedStrings.verify,
                onButtonClick = {
                    // Mock email verification
                    showVerificationDialog(
                        context = navController.context,
                        title = localizedStrings.emailVerification,
                        onVerify = { isEmailVerified = true },
                        language = currentLanguage
                    )
                },
                isDarkTheme = isDarkTheme,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardBg = cardBg,
                cardBorder = cardBorder
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Phone Verification Card
            SettingsCard(
                icon = Icons.Default.Phone,
                title = localizedStrings.phoneVerification,
                subtitle = if (isPhoneVerified) localizedStrings.verified else localizedStrings.notVerified,
                isVerified = isPhoneVerified,
                showButton = !isPhoneVerified,
                buttonText = localizedStrings.verify,
                onButtonClick = {
                    // Mock phone verification
                    showVerificationDialog(
                        context = navController.context,
                        title = localizedStrings.phoneVerification,
                        onVerify = { isPhoneVerified = true },
                        language = currentLanguage
                    )
                },
                isDarkTheme = isDarkTheme,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardBg = cardBg,
                cardBorder = cardBorder
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Theme Toggle Card
            ThemeToggleCard(
                isDarkTheme = isDarkTheme,
                onThemeToggle = { themeViewModel.toggleTheme() },
                localizedStrings = localizedStrings,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardBg = cardBg,
                cardBorder = cardBorder
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Check for Update Card
            SettingsActionCard(
                icon = Icons.Default.Update,
                title = localizedStrings.checkForUpdate,
                subtitle = localizedStrings.checkForUpdateDesc,
                onClick = {
                    showUpdateDialog(
                        context = navController.context,
                        language = currentLanguage
                    )
                },
                isDarkTheme = isDarkTheme,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardBg = cardBg,
                cardBorder = cardBorder
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Sign Out Card (before version)
            SettingsActionCard(
                icon = Icons.Default.Logout,
                title = localizedStrings.signOut,
                subtitle = localizedStrings.signOutDesc,
                onClick = {
                    showSignOutDialog(
                        context = navController.context,
                        navController = navController,
                        language = currentLanguage
                    )
                },
                isDarkTheme = isDarkTheme,
                textPrimary = textPrimary,
                textSecondary = textSecondary,
                cardBg = cardBg,
                cardBorder = cardBorder,
                isSignOut = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 6. Version at bottom
            Text(
                text = "${localizedStrings.version} 1.0",
                style = MaterialTheme.typography.bodySmall,
                color = textSecondary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SettingsCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isVerified: Boolean,
    showButton: Boolean,
    buttonText: String,
    onButtonClick: () -> Unit,
    isDarkTheme: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    cardBg: Color,
    cardBorder: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonPurple.copy(alpha = 0.1f),
                spotColor = NeonCyan.copy(alpha = 0.1f)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBg, cardBg.copy(alpha = 0.5f))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBorder, cardBorder.copy(alpha = 0.3f))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    NeonCyan.copy(alpha = 0.2f),
                                    NeonPurple.copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = textPrimary
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isVerified) NeonGreen else textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isVerified) NeonGreen else textSecondary
                        )
                    }
                }
                
                if (showButton) {
                    Button(
                        onClick = onButtonClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonCyan.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = buttonText,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ThemeToggleCard(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    localizedStrings: SettingsStrings,
    textPrimary: Color,
    textSecondary: Color,
    cardBg: Color,
    cardBorder: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = NeonPurple.copy(alpha = 0.1f),
                spotColor = NeonCyan.copy(alpha = 0.1f)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBg, cardBg.copy(alpha = 0.5f))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBorder, cardBorder.copy(alpha = 0.3f))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonCyan.copy(alpha = 0.2f),
                                NeonPurple.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = "Theme",
                    tint = NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = localizedStrings.theme,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = textPrimary
                )
                Text(
                    text = if (isDarkTheme) localizedStrings.darkMode else localizedStrings.lightMode,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )
            }
            
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonCyan,
                    checkedTrackColor = NeonCyan.copy(alpha = 0.3f),
                    uncheckedThumbColor = textSecondary,
                    uncheckedTrackColor = textSecondary.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun SettingsActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    textPrimary: Color,
    textSecondary: Color,
    cardBg: Color,
    cardBorder: Color,
    isSignOut: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isSignOut) Color.Red.copy(alpha = 0.1f) else NeonPurple.copy(alpha = 0.1f),
                spotColor = if (isSignOut) Color.Red.copy(alpha = 0.1f) else NeonCyan.copy(alpha = 0.1f)
            )
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBg, cardBg.copy(alpha = 0.5f))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(cardBorder, cardBorder.copy(alpha = 0.3f))
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                if (isSignOut) Color.Red.copy(alpha = 0.2f) else NeonCyan.copy(alpha = 0.2f),
                                if (isSignOut) Color.Red.copy(alpha = 0.1f) else NeonPurple.copy(alpha = 0.2f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSignOut) Color.Red else NeonCyan,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isSignOut) Color.Red else textPrimary
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = textSecondary
                )
            }
        }
    }
}

private fun showVerificationDialog(
    context: Context,
    title: String,
    onVerify: () -> Unit,
    language: AppLanguage
) {
    val message = when (language) {
        AppLanguage.ENGLISH -> "A verification code has been sent. Please verify your $title."
        AppLanguage.HINDI -> "एक सत्यापन कोड भेजा गया है। कृपया अपना $title सत्यापित करें।"
        AppLanguage.KANNADA -> "ಒಂದು ಪರಿಶೀಲನಾ ಕೋಡ್ ಕಳುಹಿಸಲಾಗಿದೆ. ದಯವಿಟ್ಟು ನಿಮ್ಮ $title ಅನ್ನು ಪರಿಶೀಲಿಸಿ."
    }
    
    val buttonText = when (language) {
        AppLanguage.ENGLISH -> "OK"
        AppLanguage.HINDI -> "ठीक है"
        AppLanguage.KANNADA -> "ಸರಿ"
    }

    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(buttonText) { _, _ ->
            onVerify()
        }
        .setNegativeButton(if (language == AppLanguage.ENGLISH) "Cancel" else if (language == AppLanguage.HINDI) "रद्द करें" else "ರದ್ದುಮಾಡಿ") { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

private fun showUpdateDialog(
    context: Context,
    language: AppLanguage
) {
    val title = when (language) {
        AppLanguage.ENGLISH -> "Check for Update"
        AppLanguage.HINDI -> "अपडेट जांचें"
        AppLanguage.KANNADA -> "ಅಪ್‌ಡೇಟ್ ಪರಿಶೀಲಿಸಿ"
    }
    
    val message = when (language) {
        AppLanguage.ENGLISH -> "You are using the latest version (1.0). No updates available."
        AppLanguage.HINDI -> "आप नवीनतम संस्करण (1.0) का उपयोग कर रहे हैं। कोई अपडेट उपलब्ध नहीं है।"
        AppLanguage.KANNADA -> "ನೀವು ಇತ್ತೀಚಿನ ಆವೃತ್ತಿ (1.0) ಬಳಸುತ್ತಿದ್ದೀರಿ. ಯಾವುದೇ ಅಪ್‌ಡೇಟ್‌ಗಳು ಲಭ್ಯವಿಲ್ಲ."
    }

    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            when (language) {
                AppLanguage.ENGLISH -> "OK"
                AppLanguage.HINDI -> "ठीक है"
                AppLanguage.KANNADA -> "ಸರಿ"
            }, null
        )
        .show()
}

private fun showSignOutDialog(
    context: Context,
    navController: NavController,
    language: AppLanguage
) {
    val title = when (language) {
        AppLanguage.ENGLISH -> "Sign Out"
        AppLanguage.HINDI -> "साइन आउट"
        AppLanguage.KANNADA -> "ಸೈನ್ ಔಟ್"
    }
    
    val message = when (language) {
        AppLanguage.ENGLISH -> "Are you sure you want to sign out?"
        AppLanguage.HINDI -> "क्या आप वाकई साइन आउट करना चाहते हैं?"
        AppLanguage.KANNADA -> "ನೀವು ನಿಜವಾಗಿಯೂ ಸೈನ್ ಔಟ್ ಮಾಡಲು ಬಯಸುವಿರಾ?"
    }

    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            when (language) {
                AppLanguage.ENGLISH -> "Sign Out"
                AppLanguage.HINDI -> "साइन आउट"
                AppLanguage.KANNADA -> "ಸೈನ್ ಔಟ್"
            }
        ) { _, _ ->
            navController.navigate("/") {
                popUpTo("/") { inclusive = true }
            }
        }
        .setNegativeButton(
            when (language) {
                AppLanguage.ENGLISH -> "Cancel"
                AppLanguage.HINDI -> "रद्द करें"
                AppLanguage.KANNADA -> "ರದ್ದುಮಾಡಿ"
            }, null
        )
        .show()
}

data class SettingsStrings(
    val settings: String,
    val settingsDescription: String,
    val emailVerification: String,
    val phoneVerification: String,
    val verified: String,
    val notVerified: String,
    val verify: String,
    val theme: String,
    val darkMode: String,
    val lightMode: String,
    val checkForUpdate: String,
    val checkForUpdateDesc: String,
    val signOut: String,
    val signOutDesc: String,
    val version: String
)

fun getSettingsStrings(language: AppLanguage): SettingsStrings {
    return when (language) {
        AppLanguage.ENGLISH -> SettingsStrings(
            settings = "Settings",
            settingsDescription = "Your settings and preferences",
            emailVerification = "Email Verification",
            phoneVerification = "Phone Verification",
            verified = "Verified",
            notVerified = "Not Verified",
            verify = "Verify",
            theme = "Theme",
            darkMode = "Dark Mode",
            lightMode = "Light Mode",
            checkForUpdate = "Check for Update",
            checkForUpdateDesc = "Current version: 1.0",
            signOut = "Sign Out",
            signOutDesc = "Sign out of your account",
            version = "Version"
        )
        AppLanguage.HINDI -> SettingsStrings(
            settings = "सेटिंग्स",
            settingsDescription = "आपकी सेटिंग्स और प्राथमिकताएं",
            emailVerification = "ईमेल सत्यापन",
            phoneVerification = "फोन सत्यापन",
            verified = "सत्यापित",
            notVerified = "सत्यापित नहीं",
            verify = "सत्यापित करें",
            theme = "थीम",
            darkMode = "डार्क मोड",
            lightMode = "लाइट मोड",
            checkForUpdate = "अपडेट जांचें",
            checkForUpdateDesc = "वर्तमान संस्करण: 1.0",
            signOut = "साइन आउट",
            signOutDesc = "अपने खाते से साइन आउट करें",
            version = "संस्करण"
        )
        AppLanguage.KANNADA -> SettingsStrings(
            settings = "ಸೆಟ್ಟಿಂಗ್‌ಗಳು",
            settingsDescription = "ನಿಮ್ಮ ಸೆಟ್ಟಿಂಗ್‌ಗಳು ಮತ್ತು ಆದ್ಯತೆಗಳು",
            emailVerification = "ಇಮೇಲ್ ಪರಿಶೀಲನೆ",
            phoneVerification = "ಫೋನ್ ಪರಿಶೀಲನೆ",
            verified = "ಪರಿಶೀಲಿಸಲಾಗಿದೆ",
            notVerified = "ಪರಿಶೀಲಿಸಲಾಗಿಲ್ಲ",
            verify = "ಪರಿಶೀಲಿಸಿ",
            theme = "ಥೀಮ್",
            darkMode = "ಡಾರ್ಕ್ ಮೋಡ್",
            lightMode = "ಲೈಟ್ ಮೋಡ್",
            checkForUpdate = "ಅಪ್‌ಡೇಟ್ ಪರಿಶೀಲಿಸಿ",
            checkForUpdateDesc = "ಪ್ರಸಕ್ತ ಆವೃತ್ತಿ: 1.0",
            signOut = "ಸೈನ್ ಔಟ್",
            signOutDesc = "ನಿಮ್ಮ ಖಾತೆಯಿಂದ ಸೈನ್ ಔಟ್ ಮಾಡಿ",
            version = "ಆವೃತ್ತಿ"
        )
    }
}

