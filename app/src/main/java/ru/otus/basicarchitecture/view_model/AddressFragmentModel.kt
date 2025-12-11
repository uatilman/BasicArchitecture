package ru.otus.basicarchitecture.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase

@HiltViewModel
class AddressFragmentModel @Inject constructor(
    private val addressSuggestUseCase: AddressSuggestUseCase
): ViewModel() {


    // TODO: Implement the ViewModel
}