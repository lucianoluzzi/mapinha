package nl.com.lucianoluzzi.mapinha.domain.mapper

import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

class AddressMapper {
    fun locationResponseToModel(locationResponse: GetNearbyVenuesResponse.VenueResponse.LocationResponse) =
        VenueModel.AddressModel(
            name = locationResponse.address,
            latitude = locationResponse.latitude,
            longitude = locationResponse.longitude
        )
}