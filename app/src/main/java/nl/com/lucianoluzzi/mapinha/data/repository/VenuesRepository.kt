package nl.com.lucianoluzzi.mapinha.data.repository

import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse

interface VenuesRepository {
    @Throws(Exception::class)
    suspend fun getNearbyVenues(
        latitude: Double,
        longitude: Double
    ): List<GetNearbyVenuesResponse.VenueResponse>
}