package nl.com.lucianoluzzi.mapinha.domain.useCase

import org.koin.dsl.module

val useCaseModule = module {
    single<GetLocationUseCase> {
        GetLocationUseCaseImpl(
            locationRepository = get(),
            locationMapper = get()
        )
    }
    single<GetNearbyVenuesUseCase> {
        GetNearbyVenuesUseCaseImpl(
            venuesRepository = get(),
            venueMapper = get()
        )
    }
}