package nl.com.lucianoluzzi.mapinha.ui.mapper

import com.google.common.truth.Truth.assertThat
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given UserLocationMapper")
internal class UserLocationMapperTest {
    private val userLocationMapper = UserLocationMapper()

    @Nested
    @DisplayName("When map function called with LocationModel")
    inner class Map {

        @Test
        fun `then UserLocation is returned`() {
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0
            val locationModel = LocationModel(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )

            val userLocation = userLocationMapper.mapToUserLocation(locationModel)
            assertThat(userLocation.latitude).isEqualTo(expectedLatitude)
            assertThat(userLocation.longitude).isEqualTo(expectedLongitude)
        }
    }
}