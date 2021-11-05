package nl.com.lucianoluzzi.mapinha.ui.map.model

sealed class MapIntents {
    object LocationPermissionAllowed : MapIntents()
    object LocationPermissionDenied : MapIntents()
    data class MapDragged(
        val latitude: Double,
        val longitude: Double
    ) : MapIntents()

    data class TryAgain(
        val latitude: Double,
        val longitude: Double
    ) : MapIntents()
}
