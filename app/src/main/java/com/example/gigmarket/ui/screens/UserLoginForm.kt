package com.example.gigmarket.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.network.Provider
import com.example.gigmarket.network.WorkingHours
import com.example.gigmarket.ui.components.LocalLinkLogo
import com.example.gigmarket.ui.theme.CardBackground
import com.example.gigmarket.ui.theme.CardBorder
import com.example.gigmarket.ui.theme.DarkBackground
import com.example.gigmarket.ui.theme.DarkBackgroundSecondary
import com.example.gigmarket.ui.theme.NeonCyan
import com.example.gigmarket.ui.theme.NeonGreen
import com.example.gigmarket.ui.theme.NeonPurple
import com.example.gigmarket.ui.theme.TextSecondary
import com.example.gigmarket.viewmodel.AppLanguage
import com.example.gigmarket.viewmodel.LanguageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserLoginForm(
    navController: NavController,
    languageViewModel: LanguageViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()
    val localizedStrings = getUserLoginFormStrings(context, currentLanguage)

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var navigateToDashboard by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    // Handle navigation when OTP is verified
    LaunchedEffect(navigateToDashboard) {
        if (navigateToDashboard) {
            delay(1500)
            navController.navigate("/user-dashboard/${fullName}") {
                popUpTo("/user-login-form") { inclusive = true }
            }
        }
    }

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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = 60.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = localizedStrings.goBack,
                        tint = NeonCyan
                    )
                }
            }

            // LocalLink Logo
            LocalLinkLogo(
                fontSize = 36.sp,
                taglineFontSize = 12.sp,
                showTagline = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = localizedStrings.userLogin,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = localizedStrings.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Form Card
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
                                colors = listOf(CardBackground, DarkBackgroundSecondary.copy(alpha = 0.6f))
                            )
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(CardBorder, CardBorder.copy(alpha = 0.3f))
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        // Full Name Field
                        if (!isOtpSent) {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text(localizedStrings.fullName) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = NeonCyan
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = CardBorder,
                                    focusedLabelColor = NeonCyan,
                                    unfocusedLabelColor = TextSecondary,
                                    cursorColor = NeonCyan,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Phone Number Field
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { 
                                    if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                                        phoneNumber = it
                                    }
                                },
                                label = { Text(localizedStrings.phoneNumber) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = NeonCyan
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonCyan,
                                    unfocusedBorderColor = CardBorder,
                                    focusedLabelColor = NeonCyan,
                                    unfocusedLabelColor = TextSecondary,
                                    cursorColor = NeonCyan,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Send OTP Button
                            Button(
                                onClick = {
                                    if (fullName.isBlank()) {
                                        showError = true
                                        errorMessage = localizedStrings.pleaseFillName
                                    } else if (phoneNumber.length < 10) {
                                        showError = true
                                        errorMessage = localizedStrings.enterValidPhone
                                    } else {
                                        showError = false
                                        isLoading = true
                                        // Simulate OTP sending
                                        coroutineScope.launch {
                                            delay(1500)
                                            isLoading = false
                                            isOtpSent = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = DarkBackground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = localizedStrings.sendOtp,
                                        color = DarkBackground,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        } else {
                            // OTP Fields (for 6-digit OTP)
                            OutlinedTextField(
                                value = otp,
                                onValueChange = { 
                                    if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                        otp = it
                                    }
                                },
                                label = { Text(localizedStrings.enterOtp) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NeonGreen,
                                    unfocusedBorderColor = CardBorder,
                                    focusedLabelColor = NeonGreen,
                                    unfocusedLabelColor = TextSecondary,
                                    cursorColor = NeonGreen,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = localizedStrings.otpSentMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Verify OTP Button
                            Button(
                                onClick = {
                                    if (otp.length != 6) {
                                        showError = true
                                        errorMessage = localizedStrings.enterValidOtp
                                    } else {
                                        showError = false
                                        isLoading = true
                                        // Trigger navigation after delay
                                        coroutineScope.launch {
                                            delay(1500)
                                            isLoading = false
                                            navigateToDashboard = true
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                                shape = RoundedCornerShape(16.dp),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = DarkBackground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = localizedStrings.verifyOtp,
                                        color = DarkBackground,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Resend OTP
                            Text(
                                text = localizedStrings.resendOtp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = NeonCyan,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable {
                                        otp = ""
                                        isOtpSent = false
                                    }
                            )
                        }

                        // Error Message
                        AnimatedVisibility(
                            visible = showError,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = errorMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Red
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Go back to home link
            Text(
                text = "← ${localizedStrings.goBack}",
                style = MaterialTheme.typography.labelLarge,
                color = NeonCyan,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(8.dp)
            )
        }
    }
}

// Localization strings for User Login Form
data class UserLoginFormStrings(
    val userLogin: String,
    val subtitle: String,
    val fullName: String,
    val phoneNumber: String,
    val enterOtp: String,
    val sendOtp: String,
    val verifyOtp: String,
    val goBack: String,
    val enterValidOtp: String,
    val enterValidPhone: String,
    val pleaseFillName: String,
    val otpSentMessage: String,
    val resendOtp: String
)

fun getUserLoginFormStrings(context: Context, language: AppLanguage): UserLoginFormStrings {
    return when (language) {
        AppLanguage.ENGLISH -> UserLoginFormStrings(
            userLogin = "User Login",
            subtitle = "Enter your details to continue",
            fullName = "Full Name",
            phoneNumber = "Phone Number",
            enterOtp = "Enter 6-digit OTP",
            sendOtp = "Send OTP",
            verifyOtp = "Verify OTP",
            goBack = "Go Back",
            enterValidOtp = "Please enter a valid 6-digit OTP",
            enterValidPhone = "Please enter a valid 10-digit phone number",
            pleaseFillName = "Please enter your full name",
            otpSentMessage = "We've sent a 6-digit OTP to your phone",
            resendOtp = "Didn't receive OTP? Send again"
        )
        AppLanguage.HINDI -> UserLoginFormStrings(
            userLogin = "उपयोगकर्ता लॉगिन",
            subtitle = "जारी रखने के लिए अपना विवरण दर्ज करें",
            fullName = "पूरा नाम",
            phoneNumber = "फोन नंबर",
            enterOtp = "6 अंकों का OTP दर्ज करें",
            sendOtp = "OTP भेजें",
            verifyOtp = "OTP सत्यापित करें",
            goBack = "वापस जाएं",
            enterValidOtp = "कृपया वैध 6 अंकों का OTP दर्ज करें",
            enterValidPhone = "कृपया वैध 10 अंकों का फोन नंबर दर्ज करें",
            pleaseFillName = "कृपया अपना पूरा नाम दर्ज करें",
            otpSentMessage = "हमने आपके फोन पर 6 अंकों का OTP भेजा है",
            resendOtp = "OTP नहीं मिला? फिर से भेजें"
        )
        AppLanguage.KANNADA -> UserLoginFormStrings(
            userLogin = "ಬಳಕೆದಾರ ಲಾಗಿನ್",
            subtitle = "ಮುಂದುವರಿಸಲು ನಿಮ್ಮ ವಿವರಗಳನ್ನು ನಮೂದಿಸಿ",
            fullName = "ಪೂರ್ಣ ಹೆಸರು",
            phoneNumber = "ಫೋನ್ ಸಂಖ್ಯೆ",
            enterOtp = "6 ಅಂಕಿಗಳ OTP ನಮೂದಿಸಿ",
            sendOtp = "OTP ಕಳುಹಿಸಿ",
            verifyOtp = "OTP ಪರಿಶೀಲಿಸಿ",
            goBack = "ಹಿಂದೆ ಹೋಗಿ",
            enterValidOtp = "ಮಾನ್ಯವಾದ 6 ಅಂಕಿಗಳ OTP ನಮೂದಿಸಿ",
            enterValidPhone = "ಮಾನ್ಯವಾದ 10 ಅಂಕಿಗಳ ಫೋನ್ ಸಂಖ್ಯೆ ನಮೂದಿಸಿ",
            pleaseFillName = "ದಯವಿಟ್ಟು ನಿಮ್ಮ ಪೂರ್ಣ ಹೆಸರನ್ನು ನಮೂದಿಸಿ",
            otpSentMessage = "ನಾವು ನಿಮ್ಮ ಫೋನ್‌ಗೆ 6 ಅಂಕಿಗಳ OTP ಕಳುಹಿಸಿದ್ದೇವೆ",
            resendOtp = "OTP ಸಿಗಲಿಲ್ಲವೇ? ಮತ್ತೆ ಕಳುಹಿಸಿ"
        )
    }
}

