package nl.com.lucianoluzzi.mapinha.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class GetNearbyVenuesResponse(
    @Json(name = "response") val data: ListVenueResponse
) {
    data class ListVenueResponse(
        val venues: List<VenueResponse>
    )

    data class VenueResponse(
        val name: String,
        val location: LocationResponse,
        val categories: List<CategoryResponse>
    ) {

        @JsonClass(generateAdapter = true)
        data class LocationResponse(
            @Json(name = "address") val address: String? = null,
            @Json(name = "lat") val latitude: Double,
            @Json(name = "lng") val longitude: Double
        )

        @JsonClass(generateAdapter = true)
        data class CategoryResponse(
            val name: String,
            @Json(name = "icon") val imageResponse: IconResponse
        )

        @JsonClass(generateAdapter = true)
        data class IconResponse(
            @Json(name = "prefix") val url: String,
            val suffix: String
        )
    }
}
