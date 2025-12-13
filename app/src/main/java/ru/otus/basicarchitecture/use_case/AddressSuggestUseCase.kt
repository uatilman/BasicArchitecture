package ru.otus.basicarchitecture.use_case

import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import ru.otus.basicarchitecture.service.DaDataService

@ViewModelScoped
interface AddressSuggestUseCase {
    class Impl @Inject constructor(
        private val daDataService: DaDataService
    ) : AddressSuggestUseCase {

    }
}