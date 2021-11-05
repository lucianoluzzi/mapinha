package nl.com.lucianoluzzi.mapinha.ui.map

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import nl.com.lucianoluzzi.mapinha.domain.Response
import nl.com.lucianoluzzi.mapinha.domain.model.LocationModel
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel
import nl.com.lucianoluzzi.mapinha.domain.useCase.GetLocationUseCase
import nl.com.lucianoluzzi.mapinha.domain.useCase.GetNearbyVenuesUseCase
import nl.com.lucianoluzzi.mapinha.tests.CoroutineScopeExtension
import nl.com.lucianoluzzi.mapinha.tests.InstantExecutorExtension
import nl.com.lucianoluzzi.mapinha.ui.map.model.LocationState
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapIntents
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapState
import nl.com.lucianoluzzi.mapinha.ui.mapper.UserLocationMapper
import nl.com.lucianoluzzi.mapinha.ui.model.UserLocation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutineScopeExtension::class, InstantExecutorExtension::class)
internal class MapsViewModelTest {
    private val getLocationUseCase = mock<GetLocationUseCase>()
    private val userLocationMapper = mock<UserLocationMapper>()
    private val getNearbyVenuesUseCase = mock<GetNearbyVenuesUseCase>()
    private lateinit var viewModel: MapsViewModel

    @BeforeEach
    private fun setUp() {
        viewModel = MapsViewModel(
            getLocationUseCase = getLocationUseCase,
            getNearbyVenuesUseCase = getNearbyVenuesUseCase,
            userLocationMapper = userLocationMapper
        )
    }

    @Nested
    @DisplayName("Given initial state")
    inner class Init {
        @Test
        fun `then map state LiveData value is null`() {
            assertThat(viewModel.mapStateLiveData.value).isNull()
        }

        @Test
        fun `then location LiveData value is null`() {
            assertThat(viewModel.locationLiveData.value).isNull()
        }
    }

    @Nested
    @DisplayName("Given LocationPermissionAllowed intent passed")
    inner class LocationPermissionAllowedIntent {

        @Test
        fun `then UseCase is called`() = runBlockingTest {
            viewModel.onIntent(MapIntents.LocationPermissionAllowed)
            verify(getLocationUseCase).getLocation()
        }

        @Nested
        @DisplayName("When UseCase returns location with success")
        inner class LocationPermissionAllowed {

            @Test
            fun `then DeviceLocation is emitted`() = runBlockingTest {
                val expectedLatitude = 1.0
                val expectedLongitude = 1.0
                val locationModel = LocationModel(
                    latitude = expectedLatitude,
                    longitude = expectedLongitude
                )
                val locationResponse = Response.Success(locationModel)

                whenever(userLocationMapper.mapToUserLocation(locationModel)).thenReturn(
                    UserLocation(
                        latitude = expectedLatitude,
                        longitude = expectedLongitude
                    )
                )
                whenever(getLocationUseCase.getLocation()).thenReturn(locationResponse)
                viewModel.onIntent(MapIntents.LocationPermissionAllowed)

                val liveDataValue = viewModel.locationLiveData.value
                assertThat(liveDataValue is LocationState.DeviceLocation).isTrue()
                val successResponse = liveDataValue as LocationState.DeviceLocation
                assertThat(successResponse.location.latitude).isEqualTo(expectedLatitude)
                assertThat(successResponse.location.longitude).isEqualTo(expectedLongitude)
            }
        }

        @Nested
        @DisplayName("When location response is Error")
        inner class LocationError {

            @Test
            fun `then DefaultLocation is emitted with permission denial indicator as false`() =
                runBlockingTest {
                    val errorResponse = Response.Error(Exception())

                    whenever(getLocationUseCase.getLocation()).thenReturn(errorResponse)
                    viewModel.onIntent(MapIntents.LocationPermissionAllowed)

                    val liveDataValue = viewModel.locationLiveData.value
                    assertThat(liveDataValue is LocationState.DefaultLocation).isTrue()
                    val defaultLocationState = liveDataValue as LocationState.DefaultLocation
                    assertThat(defaultLocationState.hasDeniedLocationPermission).isFalse()
                }
        }
    }

    @Nested
    @DisplayName("Given PermissionDeniedIntent is passed")
    inner class PermissionDeniedIntent {

        @Test
        fun `then DefaultLocation is emitted with permission denial indicator as true`() {
            viewModel.onIntent(MapIntents.LocationPermissionDenied)

            val liveDataValue = viewModel.locationLiveData.value
            assertThat(liveDataValue is LocationState.DefaultLocation).isTrue()
            val defaultLocationState = liveDataValue as LocationState.DefaultLocation
            assertThat(defaultLocationState.hasDeniedLocationPermission).isTrue()
        }
    }

    @Nested
    @DisplayName("Given MapDragged intent passed")
    inner class MapDraggedIntent {

        @Test
        fun `then UseCase is called`() = runBlockingTest {
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0
            val mapDragged = MapIntents.MapDragged(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )

            viewModel.onIntent(mapDragged)
            verify(getNearbyVenuesUseCase).getNearbyVenues(
                LocationModel(
                    latitude = expectedLatitude,
                    longitude = expectedLongitude
                )
            )
        }

        @Nested
        @DisplayName("When venues returned by UseCase")
        inner class VenuesReturned {

            @Test
            fun `then ExplorationMode is emitted`() = runBlockingTest {
                val mapDragged = MapIntents.MapDragged(
                    latitude = 1.0,
                    longitude = 1.0
                )
                val locationModel = LocationModel(
                    latitude = mapDragged.latitude,
                    longitude = mapDragged.longitude
                )
                val expectedVenueList = listOf(
                    VenueModel(
                        name = "First venue",
                        addressModel = VenueModel.AddressModel(
                            name = null,
                            latitude = 0.0,
                            longitude = 1.0
                        ),
                        image = null,
                        category = null
                    ),
                    VenueModel(
                        name = "First venue",
                        category = "Bar",
                        addressModel = VenueModel.AddressModel(
                            name = "Street",
                            latitude = 0.0,
                            longitude = 1.0
                        ),
                        image = "image!"
                    )
                )

                whenever(
                    getNearbyVenuesUseCase.getNearbyVenues(locationModel)
                ).thenReturn(Response.Success(expectedVenueList))
                viewModel.onIntent(mapDragged)

                val mapState = viewModel.mapStateLiveData.value
                assertThat(mapState is MapState.ExplorationMode).isTrue()
                val explorationMode = mapState as MapState.ExplorationMode
                assertThat(explorationMode.venues).isEqualTo(expectedVenueList)
            }
        }

        @Nested
        @DisplayName("When error is returned by UseCase")
        inner class ErrorReturned {

            @Test
            fun `then error state is emitted`() = runBlockingTest {
                val mapDragged = MapIntents.MapDragged(
                    latitude = 1.0,
                    longitude = 1.0
                )
                whenever(
                    getNearbyVenuesUseCase.getNearbyVenues(any())
                ).thenReturn(Response.Error(Exception()))
                viewModel.onIntent(mapDragged)

                val mapState = viewModel.mapStateLiveData.value
                assertThat(mapState is MapState.Error).isTrue()
            }
        }
    }

    @Nested
    @DisplayName("Given TryAgain intent passed")
    inner class TryAgainIntent {

        @Test
        fun `then UseCase is called`() = runBlockingTest {
            val expectedLatitude = 1.0
            val expectedLongitude = 1.0
            val tryAgain = MapIntents.TryAgain(
                latitude = expectedLatitude,
                longitude = expectedLongitude
            )

            viewModel.onIntent(tryAgain)
            verify(getNearbyVenuesUseCase).getNearbyVenues(
                LocationModel(
                    latitude = expectedLatitude,
                    longitude = expectedLongitude
                )
            )
        }

        @Nested
        @DisplayName("When venues returned by UseCase")
        inner class VenuesReturned {

            @Test
            fun `then ExplorationMode is emitted`() = runBlockingTest {
                val tryAgain = MapIntents.TryAgain(
                    latitude = 1.0,
                    longitude = 1.0
                )
                val locationModel = LocationModel(
                    latitude = tryAgain.latitude,
                    longitude = tryAgain.longitude
                )
                val expectedVenueList = listOf(
                    VenueModel(
                        name = "First venue",
                        addressModel = VenueModel.AddressModel(
                            name = null,
                            latitude = 0.0,
                            longitude = 1.0
                        ),
                        image = null,
                        category = null
                    ),
                    VenueModel(
                        name = "First venue",
                        category = "Bar",
                        addressModel = VenueModel.AddressModel(
                            name = "Street",
                            latitude = 0.0,
                            longitude = 1.0
                        ),
                        image = "image!"
                    )
                )

                whenever(
                    getNearbyVenuesUseCase.getNearbyVenues(locationModel)
                ).thenReturn(Response.Success(expectedVenueList))
                viewModel.onIntent(tryAgain)

                val mapState = viewModel.mapStateLiveData.value
                assertThat(mapState is MapState.ExplorationMode).isTrue()
                val explorationMode = mapState as MapState.ExplorationMode
                assertThat(explorationMode.venues).isEqualTo(expectedVenueList)
            }
        }

        @Nested
        @DisplayName("When error is returned by UseCase")
        inner class ErrorReturned {

            @Test
            fun `then error state is emitted`() = runBlockingTest {
                val tryAgain = MapIntents.TryAgain(
                    latitude = 1.0,
                    longitude = 1.0
                )
                whenever(
                    getNearbyVenuesUseCase.getNearbyVenues(any())
                ).thenReturn(Response.Error(Exception()))
                viewModel.onIntent(tryAgain)

                val mapState = viewModel.mapStateLiveData.value
                assertThat(mapState is MapState.Error).isTrue()
            }
        }
    }
}