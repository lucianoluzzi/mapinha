package nl.com.lucianoluzzi.mapinha.domain.mapper

import org.koin.dsl.module

val dataMapperModule = module {
    single {
        LocationMapper()
    }
    single {
        val addressMapper = AddressMapper()
        VenueMapper(
            addressMapper = addressMapper
        )
    }
}