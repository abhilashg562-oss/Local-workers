package com.example.gigmarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gigmarket.network.Provider
import com.example.gigmarket.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProviderViewModel : ViewModel() {

    private val _providers = MutableStateFlow<List<Provider>>(emptyList())
    val providers: StateFlow<List<Provider>> = _providers

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedService = MutableStateFlow<String?>(null)
    val selectedService: StateFlow<String?> = _selectedService

    // Default location (can be updated with user's actual location)
    private var userLat: Double = 12.97  // Default: Bangalore
    private var userLng: Double = 77.59

    fun updateLocation(lat: Double, lng: Double) {
        userLat = lat
        userLng = lng
    }

    fun selectService(serviceName: String) {
        _selectedService.value = serviceName
        fetchNearbyProviders(serviceName)
    }

    fun clearSelection() {
        _selectedService.value = null
        _providers.value = emptyList()
    }

    fun fetchNearbyProviders(category: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = RetrofitClient.providerApi.getNearbyProviders(
                    latitude = userLat,
                    longitude = userLng,
                    radius = 5000,
                    category = category
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    _providers.value = response.body()?.providers ?: emptyList()
                } else {
                    _error.value = "No workers found nearby"
                    // For demo purposes, populate with mock data if empty
                    _providers.value = getMockProviders(category)
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch providers"
                // For demo purposes, show mock data when API is unavailable
                _providers.value = getMockProviders(category)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Mock data for demonstration - remove when backend is connected
    private fun getMockProviders(category: String?): List<Provider> {
        val mockProviders = listOf(
            // Electricians
            Provider(
                _id = "1",
                name = "Rajesh Kumar",
                phone = "+91 9876543210",
                skills = listOf("Electrician", "AC Repair", "Wiring"),
                category = "Electrician",
                hourlyRate = 300.0,
                bio = "Expert electrician with 10 years experience in residential and commercial wiring. Specialized in AC repairs and electrical panel installations.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "English"),
                rating = 4.8,
                totalReviews = 45,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.59, 12.97), "MG Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "10AM - 4PM", "Closed"),
                experience = "10 years",
                certifications = listOf("ITI Certified", "Government Licensed")
            ),
            Provider(
                _id = "1a",
                name = "Vikram Sharma",
                phone = "+91 9876543216",
                skills = listOf("Electrician", "Generator Repair"),
                category = "Electrician",
                hourlyRate = 350.0,
                bio = "Specialized in industrial electrical work and generator repairs. Available for emergency services.",
                photo = null,
                languages = listOf("Hindi", "English", "Punjabi"),
                rating = 4.6,
                totalReviews = 28,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.62, 12.98), "Richmond Town, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "9AM - 6PM", "Emergency Only"),
                experience = "8 years",
                certifications = listOf("Diploma in Electrical Engineering")
            ),
            Provider(
                _id = "1b",
                name = "Mahesh Gowda",
                phone = "+91 9876543217",
                skills = listOf("Electrician", "Solar Panel Installation"),
                category = "Electrician",
                hourlyRate = 400.0,
                bio = "Expert in solar panel installation and smart home wiring solutions.",
                photo = null,
                languages = listOf("Kannada", "English"),
                rating = 4.9,
                totalReviews = 52,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.55, 12.95), "Jayanagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "10AM - 5PM", "Closed"),
                experience = "12 years",
                certifications = listOf("Solar Energy Certified", "BSES Authorized")
            ),

            // Plumbers
            Provider(
                _id = "2",
                name = "Mohammad Shaikh",
                phone = "+91 9876543211",
                skills = listOf("Plumber", "RO Water Purifier", "Pipe Fitting"),
                category = "Plumber",
                hourlyRate = 250.0,
                bio = "Professional plumber for all your needs. Expert in leak repairs, pipe fitting, and RO system installation.",
                photo = null,
                languages = listOf("Kannada", "Hindi"),
                rating = 4.5,
                totalReviews = 32,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.60, 12.96), "Brigade Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "9AM - 5PM", "Closed"),
                experience = "7 years",
                certifications = listOf("Licensed Plumber")
            ),
            Provider(
                _id = "2a",
                name = "Ramu Naidu",
                phone = "+91 9876543218",
                skills = listOf("Plumber", "Bathroom Renovation"),
                category = "Plumber",
                hourlyRate = 300.0,
                bio = "Complete bathroom and kitchen plumbing solutions. From fittings to full renovations.",
                photo = null,
                languages = listOf("Tamil", "Kannada", "Hindi"),
                rating = 4.7,
                totalReviews = 41,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.63, 12.94), "Shivajinagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("7AM - 6PM", "7AM - 6PM", "7AM - 6PM", "7AM - 6PM", "7AM - 6PM", "8AM - 4PM", "Closed"),
                experience = "9 years",
                certifications = listOf("Master Plumber License")
            ),
            Provider(
                _id = "2b",
                name = "Kiran Patel",
                phone = "+91 9876543219",
                skills = listOf("Plumber", "Drain Cleaning", "Water Heater"),
                category = "Plumber",
                hourlyRate = 280.0,
                bio = "24/7 emergency plumbing services. Expert in drain cleaning and water heater installations.",
                photo = null,
                languages = listOf("Hindi", "English", "Gujarati"),
                rating = 4.4,
                totalReviews = 23,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.58, 12.99), "M.G. Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours"),
                experience = "6 years",
                certifications = listOf("Emergency Services Certified")
            ),

            // Carpenters
            Provider(
                _id = "3",
                name = "Suresh Babu",
                phone = "+91 9876543212",
                skills = listOf("Carpenter", "Furniture Repair", "Wood Polishing"),
                category = "Carpenter",
                hourlyRate = 400.0,
                bio = "Quality carpentry work for homes and offices. Specializing in custom furniture and restorations.",
                photo = null,
                languages = listOf("Kannada", "Tamil", "Hindi"),
                rating = 4.9,
                totalReviews = 67,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.58, 12.98), "Church Street, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "10AM - 4PM", "Closed"),
                experience = "15 years",
                certifications = listOf("Master Carpenter", "Furniture Design Certified")
            ),
            Provider(
                _id = "3a",
                name = "Joseph D'Souza",
                phone = "+91 9876543220",
                skills = listOf("Carpenter", "Modular Kitchen", "Wardrobes"),
                category = "Carpenter",
                hourlyRate = 450.0,
                bio = "Expert in modular kitchen and wardrobe installations. Modern designs with quality craftsmanship.",
                photo = null,
                languages = listOf("English", "Konkani", "Hindi"),
                rating = 4.8,
                totalReviews = 38,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.61, 12.93), "Frazer Town, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "9AM - 5PM", "Closed"),
                experience = "12 years",
                certifications = listOf("Modular Kitchen Specialist")
            ),
            Provider(
                _id = "3b",
                name = "Naveen Kumar",
                phone = "+91 9876543221",
                skills = listOf("Carpenter", "Door Window Repair"),
                category = "Carpenter",
                hourlyRate = 350.0,
                bio = "Quick repairs for doors, windows, and wooden fixtures. On-site service available.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "Telugu"),
                rating = 4.6,
                totalReviews = 29,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.56, 12.97), "Banashankari, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 6PM", "8AM - 6PM", "8AM - 6PM", "8AM - 6PM", "8AM - 6PM", "9AM - 4PM", "Closed"),
                experience = "8 years",
                certifications = listOf("Furniture Repair Expert")
            ),

            // Drivers
            Provider(
                _id = "4",
                name = "Arun Patel",
                phone = "+91 9876543213",
                skills = listOf("Driver", "Personal Driver", "Airport Drop"),
                category = "Driver",
                hourlyRate = 500.0,
                bio = "Reliable driver with clean record. Available for personal driving, airport transfers, and outstation trips.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "English"),
                rating = 4.7,
                totalReviews = 89,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.61, 12.95), "Whitefield, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours"),
                experience = "10 years",
                certifications = listOf("Commercial Driving License", "Background Verified")
            ),
            Provider(
                _id = "4a",
                name = "Ravi Kiran",
                phone = "+91 9876543222",
                skills = listOf("Driver", "Tour Guide Driver"),
                category = "Driver",
                hourlyRate = 600.0,
                bio = "Experienced tour driver. Knows all tourist spots in and around Bangalore. Comfortable with long drives.",
                photo = null,
                languages = listOf("English", "Hindi", "Kannada", "Tamil"),
                rating = 4.9,
                totalReviews = 156,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.59, 12.96), "City Center, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("6AM - 10PM", "6AM - 10PM", "6AM - 10PM", "6AM - 10PM", "6AM - 10PM", "6AM - 10PM", "6AM - 10PM"),
                experience = "15 years",
                certifications = listOf("Tour Guide Certified")
            ),
            Provider(
                _id = "4b",
                name = "Syed Ahmed",
                phone = "+91 9876543223",
                skills = listOf("Driver", "Self-Drive Car"),
                category = "Driver",
                hourlyRate = 450.0,
                bio = "Professional driver available for daily office commute and weekend trips. Well-mannered and punctual.",
                photo = null,
                languages = listOf("Urdu", "Hindi", "Kannada"),
                rating = 4.5,
                totalReviews = 42,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.64, 12.92), "Halasuru, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "8AM - 6PM", "Closed"),
                experience = "7 years",
                certifications = listOf("Verified Driver")
            ),

            // AC Repair
            Provider(
                _id = "5",
                name = "Vijay Kumar",
                phone = "+91 9876543214",
                skills = listOf("AC Repair", "Refrigerator Repair", "Washing Machine"),
                category = "AC Repair",
                hourlyRate = 350.0,
                bio = "Authorized service center technician. Expert in all brands of AC, refrigerator, and washing machine repairs.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "Telugu"),
                rating = 4.6,
                totalReviews = 54,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.56, 12.99), "Koramangala, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "10AM - 5PM", "Closed"),
                experience = "8 years",
                certifications = listOf("LG Service Certified", "Samsung Authorized", "Voltas Certified")
            ),
            Provider(
                _id = "5a",
                name = "Prakash J",
                phone = "+91 9876543224",
                skills = listOf("AC Repair", "Central AC", "Ventilation"),
                category = "AC Repair",
                hourlyRate = 500.0,
                bio = "Specialized in central AC systems and commercial cooling solutions. Gas refilling and maintenance expert.",
                photo = null,
                languages = listOf("English", "Kannada", "Hindi"),
                rating = 4.8,
                totalReviews = 33,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.60, 12.94), "Domlur, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "9AM - 6PM", "Emergency"),
                experience = "11 years",
                certifications = listOf("Central AC Specialist", "Daikin Certified")
            ),
            Provider(
                _id = "5b",
                name = "Ganesh AC Services",
                phone = "+91 9876543225",
                skills = listOf("AC Repair", "AC Installation", "Gas Refill"),
                category = "AC Repair",
                hourlyRate = 300.0,
                bio = "Quick and affordable AC services. Gas refilling, installation, and annual maintenance contracts available.",
                photo = null,
                languages = listOf("Kannada", "Tamil", "Hindi"),
                rating = 4.4,
                totalReviews = 67,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.57, 12.95), "BTM Layout, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "8AM - 6PM", "9AM - 2PM"),
                experience = "6 years",
                certifications = listOf("AMC Service Provider")
            ),

            // Refrigerator Repair
            Provider(
                _id = "6a",
                name = "Madhusudhan R",
                phone = "+91 9876543226",
                skills = listOf("Refrigerator Repair", "Deep Freezer", "Wine Cooler"),
                category = "Refrigerator Repair",
                hourlyRate = 300.0,
                bio = "Expert refrigerator repair for all brands. Specialize in inverter refrigerators and commercial cooling.",
                photo = null,
                languages = listOf("Kannada", "English", "Hindi"),
                rating = 4.7,
                totalReviews = 45,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.62, 12.97), "Ulsoor, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "10AM - 4PM", "Closed"),
                experience = "9 years",
                certifications = listOf("Whirlpool Authorized", "Godrej Certified")
            ),
            Provider(
                _id = "6b",
                name = "Shankar refrigeration",
                phone = "+91 9876543227",
                skills = listOf("Refrigerator Repair", "AC Repair"),
                category = "Refrigerator Repair",
                hourlyRate = 280.0,
                bio = "Affordable fridge repair services. Door seal replacement, coolant refilling, and compressor issues.",
                photo = null,
                languages = listOf("Hindi", "Kannada", "Telugu"),
                rating = 4.5,
                totalReviews = 31,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.55, 12.98), "JP Nagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "9AM - 5PM", "Closed"),
                experience = "7 years",
                certifications = listOf("Refrigeration Expert")
            ),

            // Washing Machine Repair
            Provider(
                _id = "7a",
                name = "Bharath Kumar",
                phone = "+91 9876543228",
                skills = listOf("Washing Machine Repair", "Dryer Repair"),
                category = "Washing Machine",
                hourlyRate = 320.0,
                bio = "Expert washing machine repairs - front load, top load, and semi-automatic. All brands serviced.",
                photo = null,
                languages = listOf("Kannada", "Tamil", "English"),
                rating = 4.8,
                totalReviews = 58,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.58, 12.93), "Cunningham Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "9AM - 7PM", "10AM - 5PM", "Closed"),
                experience = "8 years",
                certifications = listOf("IFB Certified", "Samsung Authorized")
            ),
            Provider(
                _id = "7b",
                name = "Amit Washing Services",
                phone = "+91 9876543229",
                skills = listOf("Washing Machine Repair", "Dishwasher"),
                category = "Washing Machine",
                hourlyRate = 350.0,
                bio = "Professional washing machine and dishwasher repairs. Quick turnaround and genuine spare parts.",
                photo = null,
                languages = listOf("Hindi", "English", "Kannada"),
                rating = 4.6,
                totalReviews = 27,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.63, 12.96), "Cooke Town, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "9AM - 6PM", "Closed"),
                experience = "6 years",
                certifications = listOf("LG Service Partner")
            ),

            // RO Water Purifier
            Provider(
                _id = "8a",
                name = "Aqua Tech Solutions",
                phone = "+91 9876543230",
                skills = listOf("RO Water Purifier", "Water Softener", "UV Filter"),
                category = "RO Water Purifier",
                hourlyRate = 280.0,
                bio = "Complete water purification solutions. RO installation, servicing, filter changes, and AMC available.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "English", "Telugu"),
                rating = 4.7,
                totalReviews = 72,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.59, 12.94), "Residency Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "9AM - 6PM", "10AM - 4PM", "Closed"),
                experience = "10 years",
                certifications = listOf("Kent Certified", "Aquaguard Partner")
            ),
            Provider(
                _id = "8b",
                name = "Pure Water Services",
                phone = "+91 9876543231",
                skills = listOf("RO Water Purifier", "Installation", "Repair"),
                category = "RO Water Purifier",
                hourlyRate = 250.0,
                bio = "Quick RO service and repairs. Filter replacement, membrane cleaning, and tank issues resolved.",
                photo = null,
                languages = listOf("Hindi", "Kannada"),
                rating = 4.5,
                totalReviews = 39,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.61, 12.99), "Vasanth Nagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "8AM - 7PM", "9AM - 5PM", "Closed"),
                experience = "5 years",
                certifications = listOf("RO Specialist")
            ),

            // House Shifting
            Provider(
                _id = "9a",
                name = "Safe Move Packers",
                phone = "+91 9876543232",
                skills = listOf("House Shifting", "Packing", "Loading"),
                category = "House Shifting",
                hourlyRate = 1500.0,
                bio = "Professional house shifting services. Packing, loading, transportation, and unpacking all in one.",
                photo = null,
                languages = listOf("Hindi", "Kannada", "English", "Marathi"),
                rating = 4.8,
                totalReviews = 124,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.56, 12.92), "Majestic, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "7AM - 9PM", "By Appointment"),
                experience = "12 years",
                certifications = listOf("ISO Certified", "Insurance Available")
            ),
            Provider(
                _id = "9b",
                name = "City Relocations",
                phone = "+91 9876543233",
                skills = listOf("House Shifting", "Office Shifting", "Vehicle Transport"),
                category = "House Shifting",
                hourlyRate = 1800.0,
                bio = "Complete relocation solutions for homes and offices. Special care for fragile items and electronics.",
                photo = null,
                languages = listOf("English", "Kannada", "Hindi", "Tamil"),
                rating = 4.9,
                totalReviews = 89,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.64, 12.95), "Sadashivanagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "8AM - 8PM", "9AM - 6PM", "By Appointment"),
                experience = "15 years",
                certifications = listOf("Premium Relocation", "Verified Packers")
            ),

            // Cleaners
            Provider(
                _id = "10",
                name = "Priya Cleaning Services",
                phone = "+91 9876543215",
                skills = listOf("Cleaners", "House Shifting", "Deep Cleaning"),
                category = "Cleaners",
                hourlyRate = 200.0,
                bio = "Professional home and office cleaning. Deep cleaning, regular housekeeping, and post-construction cleaning.",
                photo = null,
                languages = listOf("Kannada", "Hindi", "English"),
                rating = 4.8,
                totalReviews = 112,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.62, 12.94), "Indiranagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("6AM - 8PM", "6AM - 8PM", "6AM - 8PM", "6AM - 8PM", "6AM - 8PM", "7AM - 6PM", "8AM - 2PM"),
                experience = "8 years",
                certifications = listOf("Professional Cleaning Certified", "Background Verified")
            ),
            Provider(
                _id = "10a",
                name = "Sparkle Home Cleaning",
                phone = "+91 9876543234",
                skills = listOf("Cleaners", "Sofa Cleaning", "Carpet Cleaning"),
                category = "Cleaners",
                hourlyRate = 250.0,
                bio = "Specialized cleaning services for sofas, carpets, and mattresses. Eco-friendly products used.",
                photo = null,
                languages = listOf("English", "Kannada", "Hindi"),
                rating = 4.7,
                totalReviews = 56,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.57, 12.96), "St. Marks Road, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("7AM - 7PM", "7AM - 7PM", "7AM - 7PM", "7AM - 7PM", "7AM - 7PM", "8AM - 5PM", "Closed"),
                experience = "5 years",
                certifications = listOf("Carpet Cleaning Specialist")
            ),
            Provider(
                _id = "10b",
                name = "Sanitize Pros",
                phone = "+91 9876543235",
                skills = listOf("Cleaners", "Sanitization", "Disinfection"),
                category = "Cleaners",
                hourlyRate = 300.0,
                bio = "COVID-safe sanitization and disinfection services. For homes, offices, and commercial spaces.",
                photo = null,
                languages = listOf("Hindi", "English", "Kannada"),
                rating = 4.9,
                totalReviews = 78,
                isVerified = true,
                isAvailable = true,
                location = com.example.gigmarket.network.LocationData("Point", listOf(77.60, 12.93), "Vivek Nagar, Bangalore"),
                workingHours = com.example.gigmarket.network.WorkingHours("8AM - 9PM", "8AM - 9PM", "8AM - 9PM", "8AM - 9PM", "8AM - 9PM", "9AM - 6PM", "By Appointment"),
                experience = "4 years",
                certifications = listOf("COVID Safety Certified", "ISO 9001")
            )
        )

        return if (category != null) {
            mockProviders.filter { it.category == category || it.skills.any { skill -> skill.contains(category, ignoreCase = true) } }
        } else {
            mockProviders
        }
    }
}

