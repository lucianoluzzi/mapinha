package nl.com.lucianoluzzi.mapinha.domain.useCase

import android.location.Location
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import nl.com.lucianoluzzi.mapinha.data.repository.LocationRepository
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.mapper.LocationMapper
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetLocationUseCaseImplTest {
    private val locationRepository = mock<LocationRepository>()
    private val locationMapper = mock<LocationMapper>()
    private val getLocationUseCaseImpl = GetLocationUseCaseImpl(
        locationRepository = locationRepository,
        locationMapper = locationMapper
    )

    @Nested
    @DisplayName("Given getLocation called")
    inner class GetLocation {

        @Test
        fun `verify repository called`() = runBlockingTest {
            getLocationUseCaseImpl.getLocation()
            verify(locationRepository).getLocation()
        }

        @Nested
        @DisplayName("When repository returns a location")
        inner class ValidResponse {

            @Test
            fun `then returns Success`() = runBlockingTest {
                val mockLocation = mock<Location>()

                whenever(mockLocation.latitude).thenReturn(1.0)
                whenever(mockLocation.longitude).thenReturn(1.0)
                whenever(locationRepository.getLocation()).thenReturn(mockLocation)
                whenever(locationMapper.mapToLocationModel(mockLocation)).thenReturn(mock())
                val response = getLocationUseCaseImpl.getLocation()

                assertThat(response is Response.Success).isTrue()
            }

            @Test
            fun `then values returned are same passed by repository`() = runBlockingTest {
                val mockLocation = mock<Location>()
                val mockLocationModel = mock<LocationModel>()
                val expectedLatitude = 1.0
                val expectedLongitude = 1.0

                whenever(mockLocation.latitude).thenReturn(expectedLatitude)
                whenever(mockLocation.longitude).thenReturn(expectedLongitude)
                whenever(mockLocationModel.latitude).thenReturn(expectedLatitude)
                whenever(mockLocationModel.longitude).thenReturn(expectedLongitude)
                whenever(locationRepository.getLocation()).thenReturn(mockLocation)
                whenever(locationMapper.mapToLocationModel(mockLocation)).thenReturn(
                    mockLocationModel
                )
                val response = getLocationUseCaseImpl.getLocation()

                assertThat(response is Response.Success).isTrue()
                val successResponse =
                    response as Response.Success<LocationModel>
                assertThat(successResponse.data.latitude).isEqualTo(expectedLatitude)
                assertThat(successResponse.data.longitude).isEqualTo(expectedLongitude)
            }
        }

        @Nested
        @DisplayName("When repository throws SecurityException")
        inner class ThrowsSecurityException {

            @Test
            fun `then returns Error with exception as data`() = runBlockingTest {
                val securityException = SecurityException()

                whenever(locationRepository.getLocation()).thenThrow(securityException)
                val locationResponse = getLocationUseCaseImpl.getLocation()

                assertThat(locationResponse is Response.Error).isTrue()
                val responseError = locationResponse as Response.Error
                assertThat(responseError.error).isEqualTo(securityException)
            }
        }

        @Nested
        @DisplayName("When repository throws Exception")
        inner class ThrowsException {

            @Test
            fun `then returns Error with exception as data`() = runBlockingTest {
                val exception = Exception()

                whenever(locationRepository.getLocation()).thenThrow(exception)
                val locationResponse = getLocationUseCaseImpl.getLocation()

                assertThat(locationResponse is Response.Error).isTrue()
                val responseError = locationResponse as Response.Error
                assertThat(responseError.error).isEqualTo(exception)
            }
        }
    }
}