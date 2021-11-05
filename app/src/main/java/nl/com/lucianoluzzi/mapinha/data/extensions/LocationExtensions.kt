package nl.com.lucianoluzzi.mapinha.data.extensions

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun FusedLocationProviderClient.awaitLastLocation(): Location {
    return suspendCoroutine { continuation ->
        try {
            lastLocation.addOnSuccessListener {
                continuation.resume(it)
            }.addOnCanceledListener {
                continuation.resumeWithException(Exception("Location request was canceled"))
            }.addOnFailureListener {
                continuation.resumeWithException(it)
            }
        } catch (securityException: SecurityException) {
            continuation.resumeWithException(securityException)
        }
    }
}