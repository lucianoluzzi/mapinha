package nl.com.lucianoluzzi.mapinha.ui.map.model

import nl.com.lucianoluzzi.mapinha.domain.model.VenueModel

sealed class MapState {
    data class ExplorationMode(
        val venues: List<VenueModel>
    ) : MapState()
    object Error : MapState()
}
