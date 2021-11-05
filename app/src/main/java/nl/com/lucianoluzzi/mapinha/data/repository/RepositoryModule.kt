package nl.com.lucianoluzzi.mapinha.data.repository

import org.koin.dsl.module

val repositoryModule = module {
    single<LocationRepository> {
        LocationRepositoryImpl(
            locationServices = get()
        )
    }
    single<VenuesRepository> {
        VenuesRepositoryImpl(
            apiRequiredData = get(),
            venuesService = get()
        )
    }
}