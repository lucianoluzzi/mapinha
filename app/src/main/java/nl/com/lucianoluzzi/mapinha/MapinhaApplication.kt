package nl.com.lucianoluzzi.mapinha

import android.app.Application
import nl.com.lucianoluzzi.mapinha.data.dataSourceModule
import nl.com.lucianoluzzi.mapinha.data.repository.repositoryModule
import nl.com.lucianoluzzi.mapinha.data.servicesModule
import nl.com.lucianoluzzi.mapinha.domain.mapper.dataMapperModule
import nl.com.lucianoluzzi.mapinha.domain.useCase.useCaseModule
import nl.com.lucianoluzzi.mapinha.ui.mapper.uiMapperModule
import nl.com.lucianoluzzi.mapinha.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.KoinExperimentalAPI
import org.koin.core.context.startKoin

@KoinExperimentalAPI
class MapinhaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MapinhaApplication)
            fragmentFactory()
            modules(
                viewModelModule,
                useCaseModule,
                dataSourceModule,
                repositoryModule,
                dataMapperModule,
                uiMapperModule,
                servicesModule
            )
        }
    }
}