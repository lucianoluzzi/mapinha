package nl.com.lucianoluzzi.mapinha.data.repository

import android.location.Location

interface LocationRepository {
    @Throws(SecurityException::class, Exception::class)
    suspend fun getLocation(): Location
}