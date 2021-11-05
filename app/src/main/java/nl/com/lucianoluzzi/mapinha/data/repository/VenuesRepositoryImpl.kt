package nl.com.lucianoluzzi.mapinha.data.repository

import nl.com.lucianoluzzi.mapinha.data.network.api.VenuesService
import nl.com.lucianoluzzi.mapinha.data.network.model.ApiRequiredData
import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse

class VenuesRepositoryImpl(
    private val apiRequiredData: ApiRequiredData,
    private val venuesService: VenuesService
) : VenuesRepository {

    override suspend fun getNearbyVenues(
        latitude: Double,
        longitude: Double
    ): List<GetNearbyVenuesResponse.VenueResponse> {
        val response = venuesService.getVenues(
            clientId = apiRequiredData.clientId,
            clientSecret = apiRequiredData.clientSecret,
            version = apiRequiredData.networkVersion,
            latlng = "$latitude,$longitude"
        )
        return response.data.venues
    }
}