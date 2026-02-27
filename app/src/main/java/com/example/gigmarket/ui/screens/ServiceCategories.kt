package com.example.gigmarket.ui.screens

object ServiceCategories {
    data class Service(
        val name: String,
        val icon: String,
        val apiCategory: String
    )

    val categories = listOf(
        Service("Electrician", "⚡", "Electrician"),
        Service("Plumber", "🔧", "Plumber"),
        Service("Carpenter", "🪚", "Carpenter"),
        Service("Driver", "🚗", "Driver"),
        Service("AC Repair", "❄️", "AC Repair"),
        Service("Refrigerator Repair", "🧊", "Refrigerator Repair"),
        Service("Washing Machine", "🧺", "Washing Machine"),
        Service("RO Water Purifier", "💧", "RO Water Purifier"),
        Service("House Shifting", "📦", "House Shifting"),
        Service("Cleaners", "🧹", "Cleaners")
    )
}

