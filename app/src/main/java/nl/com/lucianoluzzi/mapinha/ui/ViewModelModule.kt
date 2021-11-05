package nl.com.lucianoluzzi.mapinha.ui

import nl.com.lucianoluzzi.mapinha.ui.map.MapsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MapsViewModel(
            getLocationUseCase = get(),
            getNearbyVenuesUseCase = get(),
            userLocationMapper = get()
        )
    }
}