package nl.com.lucianoluzzi.mapinha.data.repository

import com.google.android.gms.location.FusedLocationProviderClient
import nl.com.lucianoluzzi.mapinha.data.extensions.awaitLastLocation

class LocationRepositoryImpl(
    private val locationServices: FusedLocationProviderClient
) : LocationRepository {

    override suspend fun getLocation() = locationServices.awaitLastLocation()
}