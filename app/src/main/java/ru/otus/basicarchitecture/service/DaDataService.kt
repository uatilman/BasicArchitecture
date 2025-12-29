package ru.otus.basicarchitecture.service

import jakarta.inject.Inject
import ru.otus.basicarchitecture.model.net.GetAddresses
import ru.otus.basicarchitecture.model.net.Suggestions


interface DaDataService {

    suspend fun getAddresses(rawAddress: String): Suggestions

    class Impl @Inject constructor(
        private val getAddressesCommand: GetAddresses
    ) : DaDataService {
        override suspend fun getAddresses(rawAddress: String) = getAddressesCommand(rawAddress)
    }

}

