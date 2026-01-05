package ru.otus.basicarchitecture.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.BuildConfig
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase
import ru.otus.basicarchitecture.view.LoadAddressesViewState
import kotlin.time.Duration

@HiltViewModel
class AddressFragmentModel @Inject constructor(
    private val addressSuggestUseCase: AddressSuggestUseCase,
    private val dataCache: WizardCache
) : ViewModel() {

    private val _addressFlow = MutableStateFlow("")
    val addressFlow = _addressFlow.asStateFlow()

    private val _state: MutableStateFlow<LoadAddressesViewState> =
        MutableStateFlow(LoadAddressesViewState.LoadAddresses())
    val state = _state.asStateFlow()

    private var loadAddressJob: Job? = null
    private val loadAddressDelay = Duration.parse(BuildConfig.LOAD_ADDRESS_DELLAY)
//    private val loadAddressDelay = 1000L

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
        _state.emit(LoadAddressesViewState.LoadAddresses())
    }

    suspend fun addressSelected() {
        _state.emit(LoadAddressesViewState.AddressSelected)
    }

    fun loadAddressVariants(rawAddress: String) {
        loadAddressJob?.cancel()
        loadAddressJob = viewModelScope.launch {
            delay(loadAddressDelay)
            _state.emit(LoadAddressesViewState.LoadingProgress)
            runCatching {
                val addressVariants: List<String> =
                    addressSuggestUseCase.findAddress(rawAddress)
                Log.i(
                    "AddressFragmentModel",
                    "AddressVariants: \n${addressVariants.joinToString("\n")}"
                )
                _state.emit(LoadAddressesViewState.Content(addressVariants))
            }.getOrElse {
                Log.e("AddressFragmentModel", "Error when loading address variants: ${it.message}")
                _state.emit(LoadAddressesViewState.LoadAddresses(it as? Exception))
            }


        }
    }

}