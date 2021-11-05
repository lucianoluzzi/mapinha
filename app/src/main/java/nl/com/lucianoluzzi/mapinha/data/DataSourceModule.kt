package nl.com.lucianoluzzi.mapinha.data

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import nl.com.lucianoluzzi.mapinha.data.network.NetworkClientProvider
import nl.com.lucianoluzzi.mapinha.data.network.api.VenuesService
import nl.com.lucianoluzzi.mapinha.data.network.model.ApiRequiredData
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataSourceModule = module {
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(androidContext())
    }
    single {
        val networkClientProvider = get() as NetworkClientProvider
        networkClientProvider.createService(VenuesService::class.java)
    }
}