package nl.com.lucianoluzzi.mapinha.data.repository

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import nl.com.lucianoluzzi.mapinha.data.network.api.VenuesService
import nl.com.lucianoluzzi.mapinha.data.network.model.ApiRequiredData
import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class VenuesRepositoryImplTest {
    private val venuesService = mock<VenuesService>()
    private val apiRequiredData = mock<ApiRequiredData>()
    private val venuesRepositoryImpl = VenuesRepositoryImpl(
        venuesService = venuesService,
        apiRequiredData = apiRequiredData
    )

    @Nested
    @DisplayName("Given getNearbyVenues called")
    inner class GetNearbyVenues {
        private val response = GetNearbyVenuesResponse(
            data = GetNearbyVenuesResponse.ListVenueResponse(
                venues = emptyList()
            )
        )
        private val expectedLatitude = 1.0
        private val expectedLongitude = 1.0
        private val expectedClientId = "client id"
        private val expectedClientSecret = "client secret"
        private val expectedNetworkVersion = "network verion"

        @BeforeEach
        private fun setUp() = runBlockingTest {
            whenever(apiRequiredData.clientId).thenReturn(expectedClientId)
            whenever(apiRequiredData.clientSecret).thenReturn(expectedClientSecret)
            whenever(apiRequiredData.networkVersion).thenReturn(expectedNetworkVersion)
            whenever(
                venuesService.getVenues(
                    clientSecret = expectedClientSecret,
                    clientId = expectedClientId,
                    version = expectedNetworkVersion,
                    latlng = "$expectedLatitude,$expectedLongitude",
                    radius = 200,
                    limit = 10
                )
            ).thenReturn(response)
        }

        @Test
        fun `then service called with parameters`() = runBlockingTest {
            venuesRepositoryImpl.getNearbyVenues(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )

            verify(venuesService).getVenues(
                clientSecret = expectedClientSecret,
                clientId = expectedClientId,
                version = expectedNetworkVersion,
                latlng = "$expectedLatitude,$expectedLongitude",
                radius = 200,
                limit = 10
            )
        }

        @Test
        fun `then returns service venue list`() = runBlockingTest {
            val repositoryResponse = venuesRepositoryImpl.getNearbyVenues(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )
            assertThat(repositoryResponse).isEqualTo(response.data.venues)
        }
    }
}