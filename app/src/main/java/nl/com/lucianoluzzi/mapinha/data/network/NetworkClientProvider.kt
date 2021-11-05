package nl.com.lucianoluzzi.mapinha.data.network

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkClientProvider(moshiClient: Moshi) {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(moshiClient)
        ).build()

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }

    private companion object {
        private const val BASE_URL = "https://api.foursquare.com/v2/"
    }
}