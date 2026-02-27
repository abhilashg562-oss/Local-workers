package com.example.gigmarket.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.network.LocationData
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
import com.example.gigmarket.viewmodel.AppLanguage
import com.example.gigmarket.viewmodel.LanguageViewModel

@Composable
fun WorkerProfileScreen(
    navController: NavController,
    workerId: String,
    workerName: String,
    workerPhone: String = "",
    workerCategory: String = "",
    workerRating: Double = 0.0,
    workerTotalReviews: Int = 0,
    workerBio: String = "",
    workerSkills: List<String> = emptyList(),
    workerLanguages: List<String> = emptyList(),
    workerHourlyRate: Double? = null,
    workerExperience: String = "",
    workerIsVerified: Boolean = false,
    workerIsAvailable: Boolean = false,
    workerLocationAddress: String = "",
    workerLocationLat: Double = 0.0,
    workerLocationLng: Double = 0.0,
    languageViewModel: LanguageViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()

    val provider = Provider(
        _id = workerId,
        name = workerName,
        phone = workerPhone,
        category = workerCategory,
        rating = workerRating,
        totalReviews = workerTotalReviews,
        bio = workerBio,
        skills = workerSkills,
        languages = workerLanguages,
        hourlyRate = workerHourlyRate,
        experience = workerExperience,
        isVerified = workerIsVerified,
        isAvailable = workerIsAvailable,
        location = LocationData("Point", listOf(workerLocationLng, workerLocationLat), workerLocationAddress),
        workingHours = null,
        photo = null
    )

    val localizedStrings = getWorkerProfileStrings(context, currentLanguage)

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
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = localizedStrings.back,
                        tint = NeonCyan
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = localizedStrings.workerProfile,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(elevation = 25.dp, shape = RoundedCornerShape(24.dp), ambientColor = NeonPurple.copy(alpha = 0.2f), spotColor = NeonCyan.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = Brush.verticalGradient(colors = listOf(CardBackground, DarkBackgroundSecondary.copy(alpha = 0.6f))))
                        .border(width = 1.dp, brush = Brush.verticalGradient(colors = listOf(NeonCyan.copy(alpha = 0.3f), NeonPurple.copy(alpha = 0.1f))), shape = RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(brush = Brush.linearGradient(colors = listOf(NeonCyan, NeonPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = provider.name.first().toString(),
                                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                                color = DarkBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = provider.name, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            if (provider.isVerified) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = NeonCyan, modifier = Modifier.size(24.dp))
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = NeonOrange, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${provider.rating}", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Text(text = " (${provider.totalReviews} ${if (provider.totalReviews == 1) "review" else "reviews"})", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }

                        if (provider.location.address?.isNotEmpty() == true) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Location", tint = NeonGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = provider.location.address ?: "", style = MaterialTheme.typography.bodyMedium, color = NeonGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier = Modifier
                                .background(color = if (provider.isAvailable) NeonGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f), shape = RoundedCornerShape(20.dp))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(text = if (provider.isAvailable) localizedStrings.availableNow else localizedStrings.notAvailable, color = if (provider.isAvailable) NeonGreen else Color.Red, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonCyan), shape = RoundedCornerShape(16.dp)) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = localizedStrings.callNow, tint = DarkBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = localizedStrings.callNow, color = DarkBackground, fontWeight = FontWeight.Bold)
                }
                Button(onClick = { }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = NeonPurple), shape = RoundedCornerShape(16.dp)) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = localizedStrings.book, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = localizedStrings.book, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(elevation = 20.dp, shape = RoundedCornerShape(20.dp), ambientColor = NeonPurple.copy(alpha = 0.1f), spotColor = NeonCyan.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(brush = Brush.verticalGradient(colors = listOf(CardBackground, DarkBackgroundSecondary.copy(alpha = 0.5f))))
                        .border(width = 1.dp, brush = Brush.verticalGradient(colors = listOf(CardBorder, CardBorder.copy(alpha = 0.3f))), shape = RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        WorkerDetailSection(icon = Icons.Default.Description, title = localizedStrings.about, content = provider.bio ?: localizedStrings.noDescription)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        WorkerDetailSection(icon = Icons.Default.Call, title = localizedStrings.phoneNumber, content = provider.phone)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        WorkerDetailSection(icon = Icons.Default.WorkHistory, title = localizedStrings.category, content = provider.category)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        WorkerDetailSection(icon = Icons.Default.WorkHistory, title = localizedStrings.experience, content = provider.experience ?: localizedStrings.notSpecified)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        provider.hourlyRate?.let { rate ->
                            WorkerDetailSection(icon = Icons.Default.Description, title = localizedStrings.hourlyRate, content = "₹${rate.toInt()}/hr")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        }
                        WorkerDetailSection(icon = Icons.Default.Language, title = localizedStrings.languages, content = provider.languages.joinToString(", "))
                        if (provider.skills.isNotEmpty()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                            Text(text = localizedStrings.skillsServices, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                provider.skills.forEach { skill ->
                                    Box(modifier = Modifier.background(color = NeonPurple.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                                        Text(text = skill, style = MaterialTheme.typography.labelMedium, color = NeonPurple)
                                    }
                                }
                            }
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = CardBorder)
                        WorkerDetailSection(icon = Icons.Default.LocationOn, title = localizedStrings.serviceArea, content = provider.location.address ?: localizedStrings.locationNotSpecified)
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun WorkerDetailSection(icon: ImageVector, title: String, content: String, customContent: @Composable (() -> Unit)? = null) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = title, tint = NeonCyan, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (customContent != null) {
            customContent()
        } else {
            Text(text = content, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}

data class WorkerProfileStrings(
    val back: String, val workerProfile: String, val workerNotFound: String, val goBack: String,
    val availableNow: String, val notAvailable: String, val callNow: String, val book: String,
    val about: String, val noDescription: String, val phoneNumber: String, val category: String,
    val experience: String, val notSpecified: String, val hourlyRate: String, val languages: String,
    val workingHours: String, val certifications: String, val skillsServices: String, val serviceArea: String,
    val locationNotSpecified: String, val monday: String, val tuesday: String, val wednesday: String,
    val thursday: String, val friday: String, val saturday: String, val sunday: String, val closed: String
)

fun getWorkerProfileStrings(context: Context, language: AppLanguage): WorkerProfileStrings {
    return when (language) {
        AppLanguage.ENGLISH -> WorkerProfileStrings("Back", "Worker Profile", "Worker not found", "Go Back", "● Available Now", "● Not Available", "Call Now", "Book", "About", "No description available", "Phone Number", "Category", "Experience", "Not specified", "Hourly Rate", "Languages", "Working Hours", "Certifications", "Skills & Services", "Service Area", "Location not specified", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Closed")
        AppLanguage.HINDI -> WorkerProfileStrings("वापस", "कार्यकर्ता प्रोफाइल", "कार्यकर्ता नहीं मिला", "वापस जाएं", "● अभी उपलब्ध", "● उपलब्ध नहीं", "अभी कॉल करें", "बुक करें", "के बारे में", "कोई विवरण उपलब्ध नहीं", "फोन नंबर", "श्रेणी", "अनुभव", "निर्दिष्ट नहीं", "प्रति घंटा दर", "भाषाएं", "कार्य समय", "प्रमाणपत्र", "कौशल और सेवाएं", "सेवा क्षेत्र", "स्थान निर्दिष्ट नहीं", "सोमवार", "मंगलवार", "बुधवार", "गुरुवार", "शुक्रवार", "शनिवार", "रविवार", "बंद")
        AppLanguage.KANNADA -> WorkerProfileStrings("ಹಿಂದೆ", "ಕಾರ್ಮಿಕ ಪ್ರೊಫೈಲ್", "ಕಾರ್ಮಿಕರು ಸಿಗಲಿಲ್ಲ", "ಹಿಂದೆ ಹೋಗಿ", "● ಈಗ ಲಭ್ಯ", "● ಲಭ್ಯವಿಲ್ಲ", "ಈಗ ಕರೆ ಮಾಡಿ", "ಬುಕ್ ಮಾಡಿ", "ಬಗ್ಗೆ", "ಯಾವುದೇ ವಿವರಣೆ ಲಭ್ಯವಿಲ್ಲ", "ಫೋನ್ ಸಂಖ್ಯೆ", "ವರ್ಗ", "ಅನುಭವ", "ನಿರ್ದಿಷ್ಟಪಡಿಸಿಲ್ಲ", "ಪ್ರತಿ ಗಂಟೆಗೆ", "ಭಾಷೆಗಳು", "ಕಾರ್ಯಾಲಯದ ಸಮಯ", "ಪ್ರಮಾಣಪತ್ರಗಳು", "ಕೌಶಲ್ಯಗಳು ಮತ್ತು ಸೇವೆಗಳು", "ಸೇವಾ ಪ್ರದೇಶ", "ಸ್ಥಳ ನಿರ್ದಿಷ್ಟಪಡಿಸಿಲ್ಲ", "ಸೋಮವಾರ", "ಮಂಗಳವಾರ", "ಬುಧವಾರ", "ಗುರುವಾರ", "ಶುಕ್ರವಾರ", "ಶನಿವಾರ", "ಭಾನುವಾರ", "ಮುಚ್ಚಿದೆ")
    }
}

