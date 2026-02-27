package com.example.gigmarket.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gigmarket.network.LocationData
import com.example.gigmarket.network.Provider
import com.example.gigmarket.network.WorkingHours
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.*

data class WorkerWithDistance(
    val provider: Provider,
    val distanceKm: Double
)

class UserLoginViewModel : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _filteredWorkers = MutableStateFlow<List<WorkerWithDistance>>(emptyList())
    val filteredWorkers: StateFlow<List<WorkerWithDistance>> = _filteredWorkers.asStateFlow()

    private val _userLat = MutableStateFlow<Double?>(null)
    val userLat: StateFlow<Double?> = _userLat.asStateFlow()

    private val _userLng = MutableStateFlow<Double?>(null)
    val userLng: StateFlow<Double?> = _userLng.asStateFlow()

    fun onCategorySelected(category: String, lat: Double, lng: Double) {
        _selectedCategory.value = category
        _userLat.value = lat
        _userLng.value = lng
        filterAndSortWorkers(category, lat, lng)
    }

    fun clearCategory() {
        _selectedCategory.value = null
        _filteredWorkers.value = emptyList()
        _userLat.value = null
        _userLng.value = null
    }

    private fun filterAndSortWorkers(category: String, lat: Double, lng: Double) {
        val filtered = getMockProviders()
            .filter { it.category.equals(category, ignoreCase = true) }
            .map { provider ->
                // coordinates: [longitude, latitude]
                val workerLat = provider.location.coordinates[1]
                val workerLng = provider.location.coordinates[0]
                WorkerWithDistance(provider, haversine(lat, lng, workerLat, workerLng))
            }
            .sortedBy { it.distanceKm }
        _filteredWorkers.value = filtered
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun getMockProviders(): List<Provider> = listOf(
        // Electricians
        Provider(
            _id = "1", name = "Rajesh Kumar", phone = "+91 9876543210",
            skills = listOf("Electrician", "AC Repair", "Wiring"), category = "Electrician",
            hourlyRate = 300.0, bio = "Expert electrician with 10 years experience.", photo = null,
            languages = listOf("Kannada", "Hindi", "English"), rating = 4.8, totalReviews = 45,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.59, 12.97), "MG Road, Bangalore"),
            workingHours = WorkingHours("9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "10AM-4PM", "Closed"),
            experience = "10 years", certifications = listOf("ITI Certified")
        ),
        Provider(
            _id = "1a", name = "Vikram Sharma", phone = "+91 9876543216",
            skills = listOf("Electrician", "Generator Repair"), category = "Electrician",
            hourlyRate = 350.0, bio = "Specialized in industrial electrical work.", photo = null,
            languages = listOf("Hindi", "English"), rating = 4.6, totalReviews = 28,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.62, 12.98), "Richmond Town, Bangalore"),
            workingHours = WorkingHours("8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "9AM-6PM", "Emergency Only"),
            experience = "8 years", certifications = listOf("Diploma in Electrical Engineering")
        ),
        Provider(
            _id = "1b", name = "Mahesh Gowda", phone = "+91 9876543217",
            skills = listOf("Electrician", "Solar Panel Installation"), category = "Electrician",
            hourlyRate = 400.0, bio = "Expert in solar panel installation.", photo = null,
            languages = listOf("Kannada", "English"), rating = 4.9, totalReviews = 52,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.55, 12.95), "Jayanagar, Bangalore"),
            workingHours = WorkingHours("9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "10AM-5PM", "Closed"),
            experience = "12 years", certifications = listOf("Solar Energy Certified")
        ),
        // Plumbers
        Provider(
            _id = "2", name = "Mohammad Shaikh", phone = "+91 9876543211",
            skills = listOf("Plumber", "RO Water Purifier", "Pipe Fitting"), category = "Plumber",
            hourlyRate = 250.0, bio = "Professional plumber for all needs.", photo = null,
            languages = listOf("Kannada", "Hindi"), rating = 4.5, totalReviews = 32,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.60, 12.96), "Brigade Road, Bangalore"),
            workingHours = WorkingHours("8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "9AM-5PM", "Closed"),
            experience = "7 years", certifications = listOf("Licensed Plumber")
        ),
        Provider(
            _id = "2a", name = "Ramu Naidu", phone = "+91 9876543218",
            skills = listOf("Plumber", "Bathroom Renovation"), category = "Plumber",
            hourlyRate = 300.0, bio = "Complete bathroom and kitchen plumbing.", photo = null,
            languages = listOf("Tamil", "Kannada", "Hindi"), rating = 4.7, totalReviews = 41,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.63, 12.94), "Shivajinagar, Bangalore"),
            workingHours = WorkingHours("7AM-6PM", "7AM-6PM", "7AM-6PM", "7AM-6PM", "7AM-6PM", "8AM-4PM", "Closed"),
            experience = "9 years", certifications = listOf("Master Plumber License")
        ),
        Provider(
            _id = "2b", name = "Kiran Patel", phone = "+91 9876543219",
            skills = listOf("Plumber", "Drain Cleaning", "Water Heater"), category = "Plumber",
            hourlyRate = 280.0, bio = "24/7 emergency plumbing services.", photo = null,
            languages = listOf("Hindi", "English", "Gujarati"), rating = 4.4, totalReviews = 23,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.58, 12.99), "M.G. Road, Bangalore"),
            workingHours = WorkingHours("24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours"),
            experience = "6 years", certifications = listOf("Emergency Services Certified")
        ),
        // Carpenters
        Provider(
            _id = "3", name = "Suresh Babu", phone = "+91 9876543212",
            skills = listOf("Carpenter", "Furniture Repair", "Wood Polishing"), category = "Carpenter",
            hourlyRate = 400.0, bio = "Quality carpentry for homes and offices.", photo = null,
            languages = listOf("Kannada", "Tamil", "Hindi"), rating = 4.9, totalReviews = 67,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.58, 12.98), "Church Street, Bangalore"),
            workingHours = WorkingHours("9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "10AM-4PM", "Closed"),
            experience = "15 years", certifications = listOf("Master Carpenter")
        ),
        Provider(
            _id = "3a", name = "Joseph D'Souza", phone = "+91 9876543220",
            skills = listOf("Carpenter", "Modular Kitchen", "Wardrobes"), category = "Carpenter",
            hourlyRate = 450.0, bio = "Modular kitchen and wardrobe installations.", photo = null,
            languages = listOf("English", "Konkani", "Hindi"), rating = 4.8, totalReviews = 38,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.61, 12.93), "Frazer Town, Bangalore"),
            workingHours = WorkingHours("8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "9AM-5PM", "Closed"),
            experience = "12 years", certifications = listOf("Modular Kitchen Specialist")
        ),
        Provider(
            _id = "3b", name = "Naveen Kumar", phone = "+91 9876543221",
            skills = listOf("Carpenter", "Door Window Repair"), category = "Carpenter",
            hourlyRate = 350.0, bio = "Quick repairs for doors, windows, and wooden fixtures.", photo = null,
            languages = listOf("Kannada", "Hindi", "Telugu"), rating = 4.6, totalReviews = 29,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.56, 12.97), "Banashankari, Bangalore"),
            workingHours = WorkingHours("8AM-6PM", "8AM-6PM", "8AM-6PM", "8AM-6PM", "8AM-6PM", "9AM-4PM", "Closed"),
            experience = "8 years", certifications = listOf("Furniture Repair Expert")
        ),
        // Drivers
        Provider(
            _id = "4", name = "Arun Patel", phone = "+91 9876543213",
            skills = listOf("Driver", "Personal Driver", "Airport Drop"), category = "Driver",
            hourlyRate = 500.0, bio = "Reliable driver, airport transfers, outstation trips.", photo = null,
            languages = listOf("Kannada", "Hindi", "English"), rating = 4.7, totalReviews = 89,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.61, 12.95), "Whitefield, Bangalore"),
            workingHours = WorkingHours("24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours", "24 Hours"),
            experience = "10 years", certifications = listOf("Commercial Driving License")
        ),
        Provider(
            _id = "4a", name = "Ravi Kiran", phone = "+91 9876543222",
            skills = listOf("Driver", "Tour Guide Driver"), category = "Driver",
            hourlyRate = 600.0, bio = "Experienced tour driver around Bangalore.", photo = null,
            languages = listOf("English", "Hindi", "Kannada", "Tamil"), rating = 4.9, totalReviews = 156,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.59, 12.96), "City Center, Bangalore"),
            workingHours = WorkingHours("6AM-10PM", "6AM-10PM", "6AM-10PM", "6AM-10PM", "6AM-10PM", "6AM-10PM", "6AM-10PM"),
            experience = "15 years", certifications = listOf("Tour Guide Certified")
        ),
        // AC Repair
        Provider(
            _id = "5", name = "Vijay Kumar", phone = "+91 9876543214",
            skills = listOf("AC Repair", "Refrigerator Repair", "Washing Machine"), category = "AC Repair",
            hourlyRate = 350.0, bio = "Authorized technician for AC and refrigerator repairs.", photo = null,
            languages = listOf("Kannada", "Hindi", "Telugu"), rating = 4.6, totalReviews = 54,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.56, 12.99), "Koramangala, Bangalore"),
            workingHours = WorkingHours("9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "10AM-5PM", "Closed"),
            experience = "8 years", certifications = listOf("LG Service Certified", "Samsung Authorized")
        ),
        Provider(
            _id = "5a", name = "Prakash J", phone = "+91 9876543224",
            skills = listOf("AC Repair", "Central AC", "Ventilation"), category = "AC Repair",
            hourlyRate = 500.0, bio = "Specialized in central AC systems.", photo = null,
            languages = listOf("English", "Kannada", "Hindi"), rating = 4.8, totalReviews = 33,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.60, 12.94), "Domlur, Bangalore"),
            workingHours = WorkingHours("8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "9AM-6PM", "Emergency"),
            experience = "11 years", certifications = listOf("Daikin Certified")
        ),
        // Refrigerator Repair
        Provider(
            _id = "6a", name = "Madhusudhan R", phone = "+91 9876543226",
            skills = listOf("Refrigerator Repair", "Deep Freezer", "Wine Cooler"), category = "Refrigerator Repair",
            hourlyRate = 300.0, bio = "Expert refrigerator repair for all brands.", photo = null,
            languages = listOf("Kannada", "English", "Hindi"), rating = 4.7, totalReviews = 45,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.62, 12.97), "Ulsoor, Bangalore"),
            workingHours = WorkingHours("9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "10AM-4PM", "Closed"),
            experience = "9 years", certifications = listOf("Whirlpool Authorized")
        ),
        Provider(
            _id = "6b", name = "Shankar Refrigeration", phone = "+91 9876543227",
            skills = listOf("Refrigerator Repair", "AC Repair"), category = "Refrigerator Repair",
            hourlyRate = 280.0, bio = "Affordable fridge repairs. Door seal, coolant, compressor.", photo = null,
            languages = listOf("Hindi", "Kannada", "Telugu"), rating = 4.5, totalReviews = 31,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.55, 12.98), "JP Nagar, Bangalore"),
            workingHours = WorkingHours("8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "8AM-7PM", "9AM-5PM", "Closed"),
            experience = "7 years", certifications = listOf("Refrigeration Expert")
        ),
        // Washing Machine
        Provider(
            _id = "7a", name = "Bharath Kumar", phone = "+91 9876543228",
            skills = listOf("Washing Machine Repair", "Dryer Repair"), category = "Washing Machine",
            hourlyRate = 320.0, bio = "All brands washing machine repair.", photo = null,
            languages = listOf("Kannada", "Tamil", "English"), rating = 4.8, totalReviews = 58,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.58, 12.93), "Cunningham Road, Bangalore"),
            workingHours = WorkingHours("9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "9AM-7PM", "10AM-5PM", "Closed"),
            experience = "8 years", certifications = listOf("IFB Certified")
        ),
        // RO Water Purifier
        Provider(
            _id = "8a", name = "Aqua Tech Solutions", phone = "+91 9876543230",
            skills = listOf("RO Water Purifier", "Water Softener", "UV Filter"), category = "RO Water Purifier",
            hourlyRate = 280.0, bio = "Complete water purification solutions.", photo = null,
            languages = listOf("Kannada", "Hindi", "English", "Telugu"), rating = 4.7, totalReviews = 72,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.59, 12.94), "Residency Road, Bangalore"),
            workingHours = WorkingHours("9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "9AM-6PM", "10AM-4PM", "Closed"),
            experience = "10 years", certifications = listOf("Kent Certified")
        ),
        // House Shifting
        Provider(
            _id = "9a", name = "Safe Move Packers", phone = "+91 9876543232",
            skills = listOf("House Shifting", "Packing", "Loading"), category = "House Shifting",
            hourlyRate = 1500.0, bio = "Professional house shifting services.", photo = null,
            languages = listOf("Hindi", "Kannada", "English"), rating = 4.8, totalReviews = 124,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.56, 12.92), "Majestic, Bangalore"),
            workingHours = WorkingHours("7AM-9PM", "7AM-9PM", "7AM-9PM", "7AM-9PM", "7AM-9PM", "7AM-9PM", "By Appointment"),
            experience = "12 years", certifications = listOf("ISO Certified")
        ),
        Provider(
            _id = "9b", name = "City Relocations", phone = "+91 9876543233",
            skills = listOf("House Shifting", "Office Shifting"), category = "House Shifting",
            hourlyRate = 1800.0, bio = "Complete relocation for homes and offices.", photo = null,
            languages = listOf("English", "Kannada", "Hindi"), rating = 4.9, totalReviews = 89,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.64, 12.95), "Sadashivanagar, Bangalore"),
            workingHours = WorkingHours("8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "8AM-8PM", "9AM-6PM", "By Appointment"),
            experience = "15 years", certifications = listOf("Premium Relocation")
        ),
        // Cleaners
        Provider(
            _id = "10", name = "Priya Cleaning Services", phone = "+91 9876543215",
            skills = listOf("Cleaners", "Deep Cleaning"), category = "Cleaners",
            hourlyRate = 200.0, bio = "Professional home and office cleaning.", photo = null,
            languages = listOf("Kannada", "Hindi", "English"), rating = 4.8, totalReviews = 112,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.62, 12.94), "Indiranagar, Bangalore"),
            workingHours = WorkingHours("6AM-8PM", "6AM-8PM", "6AM-8PM", "6AM-8PM", "6AM-8PM", "7AM-6PM", "8AM-2PM"),
            experience = "8 years", certifications = listOf("Background Verified")
        ),
        Provider(
            _id = "10a", name = "Sparkle Home Cleaning", phone = "+91 9876543234",
            skills = listOf("Cleaners", "Sofa Cleaning", "Carpet Cleaning"), category = "Cleaners",
            hourlyRate = 250.0, bio = "Sofa, carpet and mattress cleaning. Eco-friendly.", photo = null,
            languages = listOf("English", "Kannada", "Hindi"), rating = 4.7, totalReviews = 56,
            isVerified = true, isAvailable = true,
            location = LocationData("Point", listOf(77.57, 12.96), "St. Marks Road, Bangalore"),
            workingHours = WorkingHours("7AM-7PM", "7AM-7PM", "7AM-7PM", "7AM-7PM", "7AM-7PM", "8AM-5PM", "Closed"),
            experience = "5 years", certifications = listOf("Carpet Cleaning Specialist")
        )
    )
}
