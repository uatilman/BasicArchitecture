package ru.otus.basicarchitecture.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase

@HiltViewModel
class AddressFragmentModel @Inject constructor(
    private val addressSuggestUseCase: AddressSuggestUseCase,
    private val dataCache: WizardCache
) : ViewModel() {

    private val _addressFlow = MutableStateFlow("")
    val addressFlow = _addressFlow.asStateFlow()

    private val _addressVariantsFlow = MutableStateFlow<List<String>>(emptyList())
    val addressVariantsFlow = _addressVariantsFlow.asStateFlow()


    init {
        // все изменения в адресе сохраняются в кеше
        addressFlow.onEach {
            dataCache.address = it
        }.launchIn(viewModelScope)
    }

    suspend fun updateAddress(address: String): Boolean {
        if (address.isBlank()) return false
        if (_addressFlow.value != address) {
            _addressFlow.emit(address)
            return true
        }
        return false
    }

    suspend fun clearVariants() {
        _addressVariantsFlow.emit(emptyList())
    }

    fun loadAddressVariants(rawAddress: String) {
        viewModelScope.launch {
            val addressVariants: List<String> =
                addressSuggestUseCase.findAddress(rawAddress)
            println("addressVariants: \n${addressVariants.joinToString("\n")}")
            _addressVariantsFlow.emit(addressVariants)

        }
    }

}