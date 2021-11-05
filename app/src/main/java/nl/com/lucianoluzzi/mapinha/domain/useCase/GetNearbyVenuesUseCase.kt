package nl.com.lucianoluzzi.mapinha.domain.useCase

import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

interface GetNearbyVenuesUseCase {
    suspend fun getNearbyVenues(locationModel: LocationModel): Response<List<VenueModel>>
}