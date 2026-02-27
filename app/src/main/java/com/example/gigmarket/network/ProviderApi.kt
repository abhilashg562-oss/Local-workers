package com.example.gigmarket.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProviderApi {

    @GET("api/providers/nearby")
    suspend fun getNearbyProviders(
        @Query("lat") latitude: Double,
        @Query("lng") longitude: Double,
        @Query("radius") radius: Int = 5000,
        @Query("category") category: String? = null
    ): Response<NearbyProvidersResponse>
}
