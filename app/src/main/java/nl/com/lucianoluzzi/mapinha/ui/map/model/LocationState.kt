package nl.com.lucianoluzzi.mapinha.ui.map.model

import nl.com.lucianoluzzi.mapinha.ui.model.UserLocation

sealed class LocationState {
    data class DeviceLocation(val location: UserLocation) : LocationState()
    data class DefaultLocation(
        val defaultLocation: UserLocation,
        val hasDeniedLocationPermission: Boolean
    ) : LocationState()
}
