package nl.com.lucianoluzzi.mapinha.domain.mapper

import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

class VenueMapper(
    private val addressMapper: AddressMapper
) {

    fun venueResponseToModel(venueResponse: GetNearbyVenuesResponse.VenueResponse): VenueModel {
        val category = venueResponse.categories.firstOrNull()
        val image = if (category != null) {
            "${category.imageResponse.url}${category.imageResponse.suffix}"
        } else null
        return VenueModel(
            name = venueResponse.name,
            category = category?.name,
            addressModel = addressMapper.locationResponseToModel(
                locationResponse = venueResponse.location
            ),
            image = image
        )
    }
}