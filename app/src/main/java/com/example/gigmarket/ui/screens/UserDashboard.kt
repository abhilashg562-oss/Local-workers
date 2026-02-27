package com.example.gigmarket.ui.screens

import android.content.Context
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gigmarket.R
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
    viewModel: ProviderViewModel = viewModel(),
    languageViewModel: LanguageViewModel = viewModel()
) {
    val context = LocalContext.current
    val providers by viewModel.providers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val currentLanguage by languageViewModel.currentLanguage.collectAsState()

    var dashboardState by remember { mutableStateOf(DashboardState.SERVICE_SELECTION) }
    var selectedProvider by remember { mutableStateOf<Provider?>(null) }
    var showLanguageMenu by remember { mutableStateOf(false) }

    val localizedStrings = getLocalizedStrings(context, currentLanguage)
    val dashboardStrings = getDashboardStrings(context, currentLanguage)

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
                            dashboardState == DashboardState.SERVICE_SELECTION && userName.isNotEmpty() -> 
                                String.format(dashboardStrings.welcomeUser, userName)
                            dashboardState == DashboardState.SERVICE_SELECTION -> dashboardStrings.welcome
                            dashboardState == DashboardState.PROVIDER_DETAILS && selectedProvider != null -> selectedProvider!!.name
                            else -> dashboardStrings.availableWorkers
                        },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(
                        text = when {
                            dashboardState == DashboardState.PROVIDER_DETAILS -> dashboardStrings.workerDetails
                            dashboardState == DashboardState.PROVIDER_LIST && selectedService != null -> 
                                String.format(dashboardStrings.nearbyWorkers, selectedService!!)
                            else -> dashboardStrings.whatServiceNeed
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
                            contentDescription = dashboardStrings.location,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = dashboardStrings.location5km,
                            style = MaterialTheme.typography.labelSmall,
                            color = NeonCyan
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // View on Map Button
                    Text(
                        text = "📍 Map View",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        modifier = Modifier
                            .background(
                                color = NeonCyan.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable {
                                navController.navigate("/user-login")
                            }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Settings Icon
                IconButton(
                    onClick = { navController.navigate("/settings") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = dashboardStrings.settings,
                        tint = NeonCyan
                    )
                }

                // Language Selector
                Box {
                    Box(
                        modifier = Modifier
                            .clickable { showLanguageMenu = true }
                            .background(
                                color = NeonCyan.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
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
                                fontSize = 12.sp
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
                    },
                    serviceCategories = getServiceCategoriesStrings(context, currentLanguage)
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
                                text = dashboardStrings.findingWorkers,
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
                                text = dashboardStrings.noWorkersFound,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                text = dashboardStrings.tryDifferentService,
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
                                    text = dashboardStrings.browseOtherServices,
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
                                },
                                dashboardStrings = dashboardStrings
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
                    ProviderDetailsScreen(
                        provider = provider,
                        dashboardStrings = dashboardStrings
                    )
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
                    text = "← ${dashboardStrings.logout}",
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

data class DashboardStrings(
    val welcomeUser: String,
    val welcome: String,
    val workerDetails: String,
    val nearbyWorkers: String,
    val whatServiceNeed: String,
    val location: String,
    val location5km: String,
    val findingWorkers: String,
    val noWorkersFound: String,
    val tryDifferentService: String,
    val browseOtherServices: String,
    val logout: String,
    val settings: String,
    val availableWorkers: String,
    val availableNow: String,
    val notAvailable: String,
    val callNow: String,
    val book: String,
    val about: String,
    val noDescription: String,
    val phoneNumber: String,
    val experience: String,
    val notSpecified: String,
    val languages: String,
    val workingHours: String,
    val certifications: String,
    val skillsServices: String,
    val serviceArea: String,
    val locationNotSpecified: String,
    val perHour: String,
    val reviews: String,
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String,
    val closed: String
)

fun getDashboardStrings(context: Context, language: AppLanguage): DashboardStrings {
    return when (language) {
        AppLanguage.ENGLISH -> DashboardStrings(
            welcomeUser = "Hi, %1\$s! 👋",
            welcome = "Welcome! 👋",
            workerDetails = "Worker Details",
            nearbyWorkers = "Nearby %1\$s",
            whatServiceNeed = "What service do you need?",
            location = "Location",
            location5km = "5km",
            findingWorkers = "Finding workers nearby…",
            noWorkersFound = "No workers found nearby",
            tryDifferentService = "Try selecting a different service",
            browseOtherServices = "Browse Other Services",
            logout = "Logout",
            settings = "Settings",
            availableWorkers = "Available Workers",
            availableNow = "● Available Now",
            notAvailable = "● Not Available",
            callNow = "Call Now",
            book = "Book",
            about = "About",
            noDescription = "No description available",
            phoneNumber = "Phone Number",
            experience = "Experience",
            notSpecified = "Not specified",
            languages = "Languages",
            workingHours = "Working Hours",
            certifications = "Certifications",
            skillsServices = "Skills & Services",
            serviceArea = "Service Area",
            locationNotSpecified = "Location not specified",
            perHour = "/hour",
            reviews = "%1\$d reviews",
            monday = "Monday",
            tuesday = "Tuesday",
            wednesday = "Wednesday",
            thursday = "Thursday",
            friday = "Friday",
            saturday = "Saturday",
            sunday = "Sunday",
            closed = "Closed"
        )
        AppLanguage.HINDI -> DashboardStrings(
            welcomeUser = "नमस्ते, %1\$s! 👋",
            welcome = "स्वागत है! 👋",
            workerDetails = "कार्यकर्ता विवरण",
            nearbyWorkers = "आस-पास के %1\$s",
            whatServiceNeed = "आपको किस सेवा की आवश्यकता है?",
            location = "स्थान",
            location5km = "5कि.मी.",
            findingWorkers = "आस-पास के कार्यकर्ता खोज रहे हैं…",
            noWorkersFound = "आस-पास कोई कार्यकर्ता नहीं मिला",
            tryDifferentService = "कोई अन्य सेवा चुनने का प्रयास करें",
            browseOtherServices = "अन्य सेवाएं देखें",
            logout = "लॉग आउट",
            settings = "सेटिंग्स",
            availableWorkers = "उपलब्ध कार्यकर्ता",
            availableNow = "● अभी उपलब्ध",
            notAvailable = "● उपलब्ध नहीं",
            callNow = "अभी कॉल करें",
            book = "बुक करें",
            about = "के बारे में",
            noDescription = "कोई विवरण उपलब्ध नहीं",
            phoneNumber = "फोन नंबर",
            experience = "अनुभव",
            notSpecified = "निर्दिष्ट नहीं",
            languages = "भाषाएं",
            workingHours = "कार्य समय",
            certifications = "प्रमाणपत्र",
            skillsServices = "कौशल और सेवाएं",
            serviceArea = "सेवा क्षेत्र",
            locationNotSpecified = "स्थान निर्दिष्ट नहीं",
            perHour = "/घंटा",
            reviews = "%1\$d समीक्षाएं",
            monday = "सोमवार",
            tuesday = "मंगलवार",
            wednesday = "बुधवार",
            thursday = "गुरुवार",
            friday = "शुक्रवार",
            saturday = "शनिवार",
            sunday = "रविवार",
            closed = "बंद"
        )
        AppLanguage.KANNADA -> DashboardStrings(
            welcomeUser = "ನಮಸ್ತೆ, %1\$s! 👋",
            welcome = "ಸ್ವಾಗತ! 👋",
            workerDetails = "ಕಾರ್ಮಿಕ ವಿವರಗಳು",
            nearbyWorkers = "ಸಮೀಪದ %1\$s",
            whatServiceNeed = "ನಿಮಗೆ ಯಾವ ಸೇವೆ ಬೇಕು?",
            location = "ಸ್ಥಳ",
            location5km = "5ಕಿ.ಮೀ",
            findingWorkers = "ಸಮೀಪದ ಕಾರ್ಮಿಕರನ್ನು ಹುಡುಕುತ್ತಿದ್ದಾರೆ…",
            noWorkersFound = "ಸಮೀಪದಲ್ಲಿ ಯಾವುದೇ ಕಾರ್ಮಿಕರು ಸಿಗಲಿಲ್ಲ",
            tryDifferentService = "ಬೇರೆ ಸೇವೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿ ಪ್ರಯತೆ ಮಾಡಿ",
            browseOtherServices = "ಇತರ ಸೇವೆಗಳನ್ನು ವೀಕ್ಷಿಸಿ",
            logout = "ಲಾಗ್‌ಔಟ್",
            settings = "ಸೆಟ್ಟಿಂಗ್‌ಗಳು",
            availableWorkers = "ಲಭ್ಯವಿರುವ ಕಾರ್ಮಿಕರು",
            availableNow = "● ಈಗ ಲಭ್ಯ",
            notAvailable = "● ಲಭ್ಯವಿಲ್ಲ",
            callNow = "ಈಗ ಕರೆ ಮಾಡಿ",
            book = "ಬುಕ್ ಮಾಡಿ",
            about = "ಬಗ್ಗೆ",
            noDescription = "ಯಾವುದೇ ವಿವರಣೆ ಲಭ್ಯವಿಲ್ಲ",
            phoneNumber = "ಫೋನ್ ಸಂಖ್ಯೆ",
            experience = "ಅನುಭವ",
            notSpecified = "ನಿರ್ದಿಷ್ಟಪಡಿಸಿಲ್ಲ",
            languages = "ಭಾಷೆಗಳು",
            workingHours = "ಕಾರ್ಯಾಲಯದ ಸಮಯ",
            certifications = "ಪ್ರಮಾಣಪತ್ರಗಳು",
            skillsServices = "ಕೌಶಲ್ಯಗಳು ಮತ್ತು ಸೇವೆಗಳು",
            serviceArea = "ಸೇವಾ ಪ್ರದೇಶ",
            locationNotSpecified = "ಸ್ಥಳ ನಿರ್ದಿಷ್ಟಪಡಿಸಿಲ್ಲ",
            perHour = "/ಗಂಟೆ",
            reviews = "%1\$d ವಿಮರ್ಶೆಗಳು",
            monday = "ಸೋಮವಾರ",
            tuesday = "ಮಂಗಳವಾರ",
            wednesday = "ಬುಧವಾರ",
            thursday = "ಗುರುವಾರ",
            friday = "ಶುಕ್ರವಾರ",
            saturday = "ಶನಿವಾರ",
            sunday = "ಭಾನುವಾರ",
            closed = "ಮುಚ್ಚಿದೆ"
        )
    }
}

data class ServiceCategoryStrings(
    val electrician: String,
    val plumber: String,
    val carpenter: String,
    val driver: String,
    val acRepair: String,
    val refrigerator: String,
    val washingMachine: String,
    val roWater: String,
    val houseShifting: String,
    val cleaners: String
)

fun getServiceCategoriesStrings(context: Context, language: AppLanguage): ServiceCategoryStrings {
    return when (language) {
        AppLanguage.ENGLISH -> ServiceCategoryStrings(
            electrician = "Electrician",
            plumber = "Plumber",
            carpenter = "Carpenter",
            driver = "Driver",
            acRepair = "AC Repair",
            refrigerator = "Refrigerator Repair",
            washingMachine = "Washing Machine",
            roWater = "RO Water Purifier",
            houseShifting = "House Shifting",
            cleaners = "Cleaners"
        )
        AppLanguage.HINDI -> ServiceCategoryStrings(
            electrician = "इलेक्ट्रीशियन",
            plumber = "प्लंबर",
            carpenter = "कारपेंटर",
            driver = "ड्राइवर",
            acRepair = "एसी मरम्मत",
            refrigerator = "फ्रिज मरम्मत",
            washingMachine = "वाशिंग मशीन",
            roWater = "आरओ वाटर प्यूरीफायर",
            houseShifting = "घर शिफ्टिंग",
            cleaners = "क्लीनर"
        )
        AppLanguage.KANNADA -> ServiceCategoryStrings(
            electrician = "ವಿದ್ಯುತ್ ತಂತಿಗಾರ",
            plumber = "ಪ್ಲಂಬರ್",
            carpenter = "ಮರಗೆಲಸಗಾರ",
            driver = "ಚಾಲಕ",
            acRepair = "AC ದುರಸ್ತಿ",
            refrigerator = "ಫ್ರಿಜ್ ದುರಸ್ತಿ",
            washingMachine = "ವಾಶಿಂಗ್ ಮಶೀನ್",
            roWater = "RO ನೀರು ಶುದ್ಧೀಕರಣ",
            houseShifting = "ಮನೆ ಸ್ಥಳಾಂತರ",
            cleaners = "ಸ್ವಚ್ಛತಾ ಸಿಬ್ಬಂದಿ"
        )
    }
}

@Composable
fun ProviderDetailsScreen(
    provider: Provider,
    dashboardStrings: DashboardStrings
) {
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
                            text = " (${provider.totalReviews} ${if (provider.totalReviews == 1) "review" else "reviews"})",
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
                            text = if (provider.isAvailable) dashboardStrings.availableNow else dashboardStrings.notAvailable,
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
                    contentDescription = dashboardStrings.callNow,
                    tint = DarkBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dashboardStrings.callNow,
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
                    contentDescription = dashboardStrings.book,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dashboardStrings.book,
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
                        title = dashboardStrings.about,
                        content = provider.bio ?: dashboardStrings.noDescription
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Phone Number
                    DetailSection(
                        icon = Icons.Default.Call,
                        title = dashboardStrings.phoneNumber,
                        content = provider.phone
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Experience
                    DetailSection(
                        icon = Icons.Default.WorkHistory,
                        title = dashboardStrings.experience,
                        content = provider.experience ?: dashboardStrings.notSpecified
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = CardBorder
                    )

                    // Languages
                    DetailSection(
                        icon = Icons.Default.Language,
                        title = dashboardStrings.languages,
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
                            title = dashboardStrings.workingHours,
                            content = "",
                            customContent = {
                                WorkingHoursDisplay(hours, dashboardStrings)
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
                                title = dashboardStrings.certifications,
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
                            text = dashboardStrings.skillsServices,
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
                        title = dashboardStrings.serviceArea,
                        content = provider.location.address ?: dashboardStrings.locationNotSpecified
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
fun WorkingHoursDisplay(hours: WorkingHours, dashboardStrings: DashboardStrings) {
    Column {
        WorkingHoursRow(dashboardStrings.monday, hours.monday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.tuesday, hours.tuesday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.wednesday, hours.wednesday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.thursday, hours.thursday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.friday, hours.friday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.saturday, hours.saturday, dashboardStrings)
        WorkingHoursRow(dashboardStrings.sunday, hours.sunday, dashboardStrings)
    }
}

@Composable
fun WorkingHoursRow(day: String, hours: String, dashboardStrings: DashboardStrings) {
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
            color = if (hours == dashboardStrings.closed || hours == "Closed") Color.Red else NeonGreen
        )
    }
}

@Composable
fun ServiceCategoryGrid(
    onServiceClick: (String) -> Unit,
    serviceCategories: ServiceCategoryStrings
) {
    val categories = listOf(
        ServiceCategories.Service(serviceCategories.electrician, "⚡", "Electrician"),
        ServiceCategories.Service(serviceCategories.plumber, "🔧", "Plumber"),
        ServiceCategories.Service(serviceCategories.carpenter, "🪚", "Carpenter"),
        ServiceCategories.Service(serviceCategories.driver, "🚗", "Driver"),
        ServiceCategories.Service(serviceCategories.acRepair, "❄️", "AC Repair"),
        ServiceCategories.Service(serviceCategories.refrigerator, "🧊", "Refrigerator Repair"),
        ServiceCategories.Service(serviceCategories.washingMachine, "🧺", "Washing Machine"),
        ServiceCategories.Service(serviceCategories.roWater, "💧", "RO Water Purifier"),
        ServiceCategories.Service(serviceCategories.houseShifting, "📦", "House Shifting"),
        ServiceCategories.Service(serviceCategories.cleaners, "🧹", "Cleaners")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 60.dp)
    ) {
        items(categories) { service ->
            ServiceCategoryCard(
                service = service,
                onClick = { onServiceClick(service.apiCategory) }
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
fun ProviderCard(
    provider: Provider,
    onClick: () -> Unit,
    dashboardStrings: DashboardStrings
) {
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
                                    text = " (${provider.totalReviews} ${if (provider.totalReviews == 1) "review" else "reviews"})",
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
                            text = dashboardStrings.perHour,
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
                            contentDescription = dashboardStrings.location,
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
                                contentDescription = dashboardStrings.callNow,
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

