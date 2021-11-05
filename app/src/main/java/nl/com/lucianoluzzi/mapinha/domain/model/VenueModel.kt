package nl.com.lucianoluzzi.mapinha.domain.model

data class VenueModel(
    val name: String,
    val category: String?,
    val addressModel: AddressModel,
    val image: String?
) {
    data class AddressModel(
        val name: String?,
        val latitude: Double,
        val longitude: Double
    )
}