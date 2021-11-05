package nl.com.lucianoluzzi.mapinha.domain.mapper

import android.location.Location
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Location")
internal class LocationMapperTest {
    private val location = mock<Location>()
    private val locationMapper = LocationMapper()


    @Nested
    @DisplayName("When mapToLocationModel called")
    inner class MapLocationModel {

        @Test
        fun `then LocationModel returned with Location fields`() {
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0

            whenever(location.latitude).thenReturn(expectedLatitude)
            whenever(location.longitude).thenReturn(expectedLongitude)
            val mappedLocationModel = locationMapper.mapToLocationModel(location)

            assertThat(mappedLocationModel.latitude).isEqualTo(expectedLatitude)
            assertThat(mappedLocationModel.longitude).isEqualTo(expectedLongitude)
        }
    }
}