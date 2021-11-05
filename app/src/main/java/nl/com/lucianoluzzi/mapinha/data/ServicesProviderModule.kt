package nl.com.lucianoluzzi.mapinha.data

import nl.com.lucianoluzzi.mapinha.data.network.NetworkClientProvider
import nl.com.lucianoluzzi.mapinha.data.network.api.VenuesService
import nl.com.lucianoluzzi.mapinha.data.network.model.ApiRequiredData
import org.koin.dsl.module

val servicesModule = module {
    single {
        MoshiProvider().moshi
    }
    single {
        NetworkClientProvider(
            moshiClient = get()
        )
    }
    single {
        ApiRequiredData()
    }
}