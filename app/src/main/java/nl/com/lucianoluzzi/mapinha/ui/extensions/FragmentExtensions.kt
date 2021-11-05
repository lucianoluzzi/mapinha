package nl.com.lucianoluzzi.mapinha.ui.extensions

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun SupportMapFragment.awaitForMap(): GoogleMap {
    return suspendCoroutine { continuation ->
        getMapAsync {
            continuation.resume(it)
        }
    }
}