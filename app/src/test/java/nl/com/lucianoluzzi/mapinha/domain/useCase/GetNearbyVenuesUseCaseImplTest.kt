package nl.com.lucianoluzzi.mapinha.domain.useCase

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import nl.com.lucianoluzzi.mapinha.data.repository.VenuesRepository
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.mapper.VenueMapper
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel
import org.junit.jupiter.api.*

@ExperimentalCoroutinesApi
internal class GetNearbyVenuesUseCaseImplTest {
    private lateinit var venuesRepository: VenuesRepository
    private lateinit var venueMapper: VenueMapper
    private lateinit var useCase: GetNearbyVenuesUseCaseImpl

    @BeforeEach
    private fun setUp() {
        venuesRepository = mock()
        venueMapper = mock()
        useCase = GetNearbyVenuesUseCaseImpl(
            venuesRepository = venuesRepository,
            venueMapper = venueMapper
        )
    }

    @AfterEach
    private fun cleanUp() {
        reset(
            venuesRepository,
            venueMapper
        )
    }

    @Nested
    @DisplayName("Given getNearbyVenues called")
    inner class NearbyVenuesCalled {

        @Test
        fun `verify repository called with parameters`() = runBlockingTest {
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0
            val locationModel = LocationModel(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )

            useCase.getNearbyVenues(locationModel)

            verify(venuesRepository).getNearbyVenues(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )
        }

        @Nested
        @DisplayName("When repository throws exception")
        inner class RepositoryThrows {

            @Test
            fun `then returns Error`() = runBlockingTest {
                val expectedException = Exception()

                whenever(
                    venuesRepository.getNearbyVenues(
                        latitude = any(),
                        longitude = any()
                    )
                ).thenThrow(expectedException)
                val response = useCase.getNearbyVenues(
                    LocationModel(
                        latitude = 0.0,
                        longitude = 0.0
                    )
                )

                assertThat(response is Response.Error).isTrue()
            }
        }

        @Nested
        @DisplayName("When repository returns")
        inner class RepositoryReturns {

            @Test
            fun `then venueMapper called with response`() = runBlockingTest {
                val venueResponseList = listOf(
                    GetNearbyVenuesResponse.VenueResponse(
                        name = "",
                        location = GetNearbyVenuesResponse.VenueResponse.LocationResponse(
                            address = null,
                            latitude = 1.0,
                            longitude = 1.0
                        ),
                        categories = emptyList()
                    ),
                    GetNearbyVenuesResponse.VenueResponse(
                        name = "",
                        location = GetNearbyVenuesResponse.VenueResponse.LocationResponse(
                            address = null,
                            latitude = 1.0,
                            longitude = 1.0
                        ),
                        categories = emptyList()
                    ),
                    GetNearbyVenuesResponse.VenueResponse(
                        name = "",
                        location = GetNearbyVenuesResponse.VenueResponse.LocationResponse(
                            address = null,
                            latitude = 1.0,
                            longitude = 1.0
                        ),
                        categories = emptyList()
                    )
                )

                whenever(
                    venuesRepository.getNearbyVenues(
                        latitude = any(),
                        longitude = any()
                    )
                ).thenReturn(venueResponseList)
                useCase.getNearbyVenues(
                    LocationModel(
                        latitude = 0.0,
                        longitude = 0.0
                    )
                )

                verify(venueMapper, times(venueResponseList.size)).venueResponseToModel(any())
            }

            @Test
            fun `then returns Success with venues`() = runBlockingTest {
                val venueResponseList = listOf(
                    GetNearbyVenuesResponse.VenueResponse(
                        name = "",
                        location = GetNearbyVenuesResponse.VenueResponse.LocationResponse(
                            address = null,
                            latitude = 1.0,
                            longitude = 1.0
                        ),
                        categories = emptyList()
                    )
                )
                val expectedVenueModel = VenueModel(
                    name = "venue",
                    category = "category",
                    addressModel = VenueModel.AddressModel(
                        name = "",
                        latitude = 1.0,
                        longitude = 1.0
                    ),
                    image = "image"
                )

                whenever(
                    venuesRepository.getNearbyVenues(
                        latitude = 0.0,
                        longitude = 0.0
                    )
                ).thenReturn(venueResponseList)
                whenever(venueMapper.venueResponseToModel(any())).thenReturn(expectedVenueModel)
                val response = useCase.getNearbyVenues(
                    LocationModel(
                        latitude = 0.0,
                        longitude = 0.0
                    )
                )

                assertThat(response is Response.Success).isTrue()
                val responseSuccess = response as Response.Success
                assertThat(responseSuccess.data.size).isEqualTo(venueResponseList.size)
            }
        }
    }
}