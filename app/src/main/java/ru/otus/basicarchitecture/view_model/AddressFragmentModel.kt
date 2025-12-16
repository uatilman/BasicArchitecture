package ru.otus.basicarchitecture.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.otus.basicarchitecture.model.Address
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase

@HiltViewModel
class AddressFragmentModel @Inject constructor(
    private val addressSuggestUseCase: AddressSuggestUseCase
) : ViewModel() {

    private val _addressFlow = MutableStateFlow(Address())
    val addressFlow = _addressFlow.asStateFlow()

    suspend fun updateAddress(
        country: String = addressFlow.value.country,
        city: String = addressFlow.value.city,
        street: String = addressFlow.value.street
    ) {
        val newAddress = Address(country, city, street)
        _addressFlow.emit(newAddress)
       Log.i("ADDRESS_FLOW", "Emitted new address: $newAddress")
    }

}