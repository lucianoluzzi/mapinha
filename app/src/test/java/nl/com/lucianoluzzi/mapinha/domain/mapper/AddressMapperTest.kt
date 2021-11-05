package nl.com.lucianoluzzi.mapinha.domain.mapper

import com.google.common.truth.Truth.assertThat
import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given location response")
internal class AddressMapperTest {
    private val addressMapper = AddressMapper()

    @Nested
    @DisplayName("When map function is called")
    inner class Map {

        @Test
        fun `then AddressModel returned with valid values`() {
            val expectedAddress = null
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0

            val locationResponse = GetNearbyVenuesResponse.VenueResponse.LocationResponse(
                address = expectedAddress,
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )
            val locationModel = addressMapper.locationResponseToModel(locationResponse)

            assertThat(locationModel.name).isEqualTo(expectedAddress)
            assertThat(locationModel.latitude).isEqualTo(expectedLatitude)
            assertThat(locationModel.longitude).isEqualTo(expectedLongitude)
        }
    }
}