package nl.com.lucianoluzzi.mapinha.domain.mapper

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import nl.com.lucianoluzzi.mapinha.data.network.model.GetNearbyVenuesResponse
import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class VenueMapperTest {
    private val addressMapper = mock<AddressMapper>()
    private val venueMapper = VenueMapper(addressMapper)

    @Nested
    @DisplayName("Given map function called")
    inner class Map {

        @Test
        fun `then venue name is same as venue response name`() {
            val expectedName = "Venue Tapas Bar"
            val venueResponse = GetNearbyVenuesResponse.VenueResponse(
                name = expectedName,
                location = mock(),
                categories = emptyList()
            )

            whenever(addressMapper.locationResponseToModel(any())).thenReturn(
                mock()
            )
            val venueModel = venueMapper.venueResponseToModel(venueResponse)

            assertThat(venueModel.name).isEqualTo(expectedName)
        }

        @Nested
        @DisplayName("When category list is empty")
        inner class NullCategory {
            private val venueResponse = GetNearbyVenuesResponse.VenueResponse(
                name = "",
                location = mock(),
                categories = emptyList()
            )

            @Test
            fun `then venue category and image are null`() {
                whenever(addressMapper.locationResponseToModel(any())).thenReturn(
                    mock()
                )
                val venueModel = venueMapper.venueResponseToModel(venueResponse)

                assertThat(venueModel.category).isNull()
                assertThat(venueModel.image).isNull()
            }
        }

        @Nested
        @DisplayName("When category list has more than one entry")
        inner class MultipleCategories {
            private val locationResponseMock =
                mock<GetNearbyVenuesResponse.VenueResponse.LocationResponse>()

            @Test
            fun `then category name and image are taken from first entry`() {
                val expectedCategoryName = "This is the first category"
                val imageResponse = GetNearbyVenuesResponse.VenueResponse.IconResponse(
                    url = "http://teste.com/image",
                    suffix = ".jpg"
                )
                val expectedCategory = GetNearbyVenuesResponse.VenueResponse.CategoryResponse(
                    name = expectedCategoryName,
                    imageResponse = imageResponse
                )
                val venueResponse = GetNearbyVenuesResponse.VenueResponse(
                    name = "",
                    location = locationResponseMock,
                    categories = listOf(
                        expectedCategory,
                        mock(),
                        mock()
                    )
                )

                whenever(addressMapper.locationResponseToModel(locationResponseMock)).thenReturn(
                    mock()
                )
                val venueModel = venueMapper.venueResponseToModel(venueResponse)

                val expectedImage = "${imageResponse.url}${imageResponse.suffix}"
                assertThat(venueModel.image).isEqualTo(expectedImage)
                assertThat(venueModel.category).isEqualTo(expectedCategoryName)
            }
        }

        @Nested
        @DisplayName("When AddressMapper returns a AddressModel")
        inner class AddressModelReturned {

            @Test
            fun `then venue model is the returned address model`() {
                val addressModel = VenueModel.AddressModel(
                    name = "This is the street",
                    latitude = 1.0,
                    longitude = 1.0
                )
                val locationResponseMock =
                    mock<GetNearbyVenuesResponse.VenueResponse.LocationResponse>()
                val venueResponse = GetNearbyVenuesResponse.VenueResponse(
                    name = "",
                    location = locationResponseMock,
                    categories = emptyList()
                )

                whenever(addressMapper.locationResponseToModel(locationResponseMock)).thenReturn(
                    addressModel
                )
                val venueModel = venueMapper.venueResponseToModel(venueResponse)

                assertThat(venueModel.addressModel.name).isEqualTo(addressModel.name)
                assertThat(venueModel.addressModel.latitude).isEqualTo(addressModel.latitude)
                assertThat(venueModel.addressModel.longitude).isEqualTo(addressModel.longitude)
            }
        }
    }
}