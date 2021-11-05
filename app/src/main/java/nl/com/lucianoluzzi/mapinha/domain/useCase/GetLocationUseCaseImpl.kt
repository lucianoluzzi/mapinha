package nl.com.lucianoluzzi.mapinha.domain.useCase

import nl.com.lucianoluzzi.mapinha.data.repository.LocationRepository
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.mapper.LocationMapper
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel

class GetLocationUseCaseImpl(
    private val locationRepository: LocationRepository,
    private val locationMapper: LocationMapper
) : GetLocationUseCase {

    override suspend fun getLocation(): Response<LocationModel> {
        return try {
            val androidLocation = locationRepository.getLocation()
            Response.Success(
                locationMapper.mapToLocationModel(androidLocation)
            )
        } catch (exception: SecurityException) {
            Response.Error(exception)
        } catch (exception: Exception) {
            Response.Error(exception)
        }
    }
}