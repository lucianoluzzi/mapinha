package nl.com.lucianoluzzi.mapinha.data.network.api

import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VenuesService {
    @GET("venues/search")
    suspend fun getVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String,
        @Query("ll") latlng: String,
        @Query("radius") radius: Int = 200,
        @Query("limit") limit: Int = 10
    ): GetNearbyVenuesResponse
}