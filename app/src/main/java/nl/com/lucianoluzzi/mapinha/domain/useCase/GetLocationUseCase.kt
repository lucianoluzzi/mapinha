package nl.com.lucianoluzzi.mapinha.domain.useCase

import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel

interface GetLocationUseCase {
    suspend fun getLocation(): Response<LocationModel>
}