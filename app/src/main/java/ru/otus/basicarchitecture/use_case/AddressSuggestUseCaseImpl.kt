package ru.otus.basicarchitecture.use_case

import jakarta.inject.Inject
import ru.otus.basicarchitecture.service.DaDataService


class AddressSuggestUseCaseImpl : AddressSuggestUseCase {
    @set:Inject
    lateinit var daDataService: DaDataService
}