package nl.com.lucianoluzzi.mapinha.ui.mapper

import org.koin.dsl.module

val uiMapperModule = module {
    single {
        UserLocationMapper()
    }
}