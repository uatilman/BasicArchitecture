package ru.otus.basicarchitecture.use_case

import android.util.Log
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import ru.otus.basicarchitecture.service.DaDataService

@ViewModelScoped
interface AddressSuggestUseCase {

    suspend fun findAddress(rawAddress: String): List<String>

    class Impl @Inject constructor(
        private val daDataService: DaDataService
    ) : AddressSuggestUseCase {
        override suspend fun findAddress(rawAddress: String): List<String> {
            return runCatching { daDataService.getAddresses(rawAddress).suggestions.map { it.value }}
                .getOrElse {
                    Log.e("AddressSuggestUseCase", "Error while getting addresses", it)
                    listOf()
                }
        }
    }
}