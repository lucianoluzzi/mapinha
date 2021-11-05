package nl.com.lucianoluzzi.mapinha.domain.mapper

import android.location.Location
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel

class LocationMapper {
    fun mapToLocationModel(location: Location) = LocationModel(
        latitude = location.latitude,
        longitude = location.longitude
    )
}