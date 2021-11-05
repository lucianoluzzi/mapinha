package nl.com.lucianoluzzi.mapinha.domain.useCase

import nl.com.lucianoluzzi.mapinha.data.repository.VenuesRepository
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.mapper.VenueMapper
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

class GetNearbyVenuesUseCaseImpl(
    private val venuesRepository: VenuesRepository,
    private val venueMapper: VenueMapper
) : GetNearbyVenuesUseCase {

    override suspend fun getNearbyVenues(locationModel: LocationModel): Response<List<VenueModel>> {
        return try {
            val nearbyVenuesResponse = venuesRepository.getNearbyVenues(
                latitude = locationModel.latitude,
                longitude = locationModel.longitude
            )
            val venues = nearbyVenuesResponse.map {
                venueMapper.venueResponseToModel(it)
            }
            Response.Success(venues)
        } catch (exception: Exception) {
            Response.Error(exception)
        }
    }
}