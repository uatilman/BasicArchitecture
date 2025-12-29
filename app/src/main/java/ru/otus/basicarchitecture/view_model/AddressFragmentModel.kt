package ru.otus.basicarchitecture.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.model.Address
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase

@HiltViewModel
class AddressFragmentModel @Inject constructor(
    private val addressSuggestUseCase: AddressSuggestUseCase,
    private val dataCache: WizardCache
) : ViewModel() {

    private val _addressFlow = MutableStateFlow(Address())
    val addressFlow = _addressFlow.asStateFlow()

    private val _addressVariantsFlow = MutableStateFlow<List<String>>(emptyList())
    val addressVariantsFlow = _addressVariantsFlow.asStateFlow()


    init {
        addressFlow.onEach {
            dataCache.address = it
        }.launchIn(viewModelScope)
    }

    suspend fun updateAddress(
        country: String = addressFlow.value.country,
        city: String = addressFlow.value.city,
        street: String = addressFlow.value.street
    ) {
        val newAddress = Address(country, city, street)
        _addressFlow.emit(newAddress)
    }

    fun loadAddressVariants(rawAddress: String) {
        viewModelScope.launch {
            val addressVariants: List<String> =
                addressSuggestUseCase.findAddress(rawAddress).filterNotNull()
            println("addressVariants: \n${addressVariants.joinToString("\n")}")
            _addressVariantsFlow.emit(addressVariants)

        }
    }

}