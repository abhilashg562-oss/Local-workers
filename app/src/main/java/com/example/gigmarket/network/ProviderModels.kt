package com.example.gigmarket.network

data class Provider(
    val _id: String,
    val name: String,
    val phone: String,
    val skills: List<String>,
    val category: String,
    val hourlyRate: Double?,
    val bio: String?,
    val photo: String?,
    val languages: List<String>,
    val rating: Double,
    val totalReviews: Int,
    val isVerified: Boolean,
    val isAvailable: Boolean,
    val location: LocationData,
    val workingHours: WorkingHours? = null,
    val experience: String? = null,
    val certifications: List<String>? = null
)

data class WorkingHours(
    val monday: String,
    val tuesday: String,
    val wednesday: String,
    val thursday: String,
    val friday: String,
    val saturday: String,
    val sunday: String
)

data class LocationData(
    val type: String,
    val coordinates: List<Double>,
    val address: String?
)

data class NearbyProvidersResponse(
    val success: Boolean,
    val count: Int,
    val providers: List<Provider>
)
