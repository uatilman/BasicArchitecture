package ru.otus.basicarchitecture.use_case

import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import ru.otus.basicarchitecture.service.DaDataService

@ViewModelScoped
interface AddressSuggestUseCase {

    suspend fun findAddress(rawAddress: String): List<String?>

    class Impl @Inject constructor(
        private val daDataService: DaDataService
    ) : AddressSuggestUseCase {
        override suspend fun findAddress(rawAddress: String): List<String?> {
            return daDataService.getAddresses(rawAddress).suggestions.map { it.value }
        }
    }
}