package nl.com.lucianoluzzi.mapinha.ui.mapper

import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.ui.model.UserLocation

class UserLocationMapper {
    fun mapToUserLocation(locationModel: LocationModel) = UserLocation(
        latitude = locationModel.latitude,
        longitude = locationModel.longitude
    )
}