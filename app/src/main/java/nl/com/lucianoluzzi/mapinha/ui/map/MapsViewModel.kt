package nl.com.lucianoluzzi.mapinha.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.domain.useCase.GetLocationUseCase
import nl.com.lucianoluzzi.mapinha.domain.useCase.GetNearbyVenuesUseCase
import nl.com.lucianoluzzi.mapinha.ui.map.model.LocationState
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapIntents
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapState
import nl.com.lucianoluzzi.mapinha.ui.mapper.UserLocationMapper
import nl.com.lucianoluzzi.mapinha.ui.model.UserLocation

class MapsViewModel(
    private val getLocationUseCase: GetLocationUseCase,
    private val getNearbyVenuesUseCase: GetNearbyVenuesUseCase,
    private val userLocationMapper: UserLocationMapper
) : ViewModel() {
    private val _locationLiveData = MutableLiveData<LocationState>()
    val locationLiveData: LiveData<LocationState> = _locationLiveData
    private val _mapStateLiveData = MutableLiveData<MapState>()
    val mapStateLiveData: LiveData<MapState> = _mapStateLiveData

    fun onIntent(intent: MapIntents) {
        when (intent) {
            MapIntents.LocationPermissionAllowed -> requestLocation()
            MapIntents.LocationPermissionDenied -> {
                _locationLiveData.value = LocationState.DefaultLocation(
                    defaultLocation = defaultLocation,
                    hasDeniedLocationPermission = true
                )
            }
            is MapIntents.MapDragged -> requestNearbyVenues(
                latitude = intent.latitude,
                longitude = intent.longitude,
                coroutineScope = viewModelScope
            )
            is MapIntents.TryAgain -> requestNearbyVenues(
                latitude = intent.latitude,
                longitude = intent.longitude,
                coroutineScope = viewModelScope
            )
        }
    }

    private fun requestLocation() {
        viewModelScope.launch {
            when (val locationResponse = getLocationUseCase.getLocation()) {
                is Response.Success -> {
                    val userLocation = userLocationMapper.mapToUserLocation(locationResponse.data)
                    _locationLiveData.value = LocationState.DeviceLocation(userLocation)
                    requestNearbyVenues(
                        latitude = userLocation.latitude,
                        longitude = userLocation.longitude,
                        coroutineScope = this
                    )
                }
                is Response.Error -> {
                    _locationLiveData.value = LocationState.DefaultLocation(
                        defaultLocation = defaultLocation,
                        hasDeniedLocationPermission = false
                    )
                }
            }
        }
    }

    private fun requestNearbyVenues(
        latitude: Double,
        longitude: Double,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            val nearbyVenuesResponse = getNearbyVenuesUseCase.getNearbyVenues(
                LocationModel(
                    latitude = latitude,
                    longitude = longitude
                )
            )
            when (nearbyVenuesResponse) {
                is Response.Error -> _mapStateLiveData.value = MapState.Error
                is Response.Success -> _mapStateLiveData.value = MapState.ExplorationMode(
                    venues = nearbyVenuesResponse.data
                )
            }
        }
    }

    private companion object {
        private val defaultLocation = UserLocation(
            latitude = -30.040635359620754,
            longitude = -51.21872310964534
        )
    }
}