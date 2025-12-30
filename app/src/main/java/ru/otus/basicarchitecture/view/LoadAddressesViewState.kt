package ru.otus.basicarchitecture.view


sealed class LoadAddressesViewState {
    /**
     * Загрузка не началась
     * */
    data class LoadAddresses(val error: Exception? = null) : LoadAddressesViewState()
    /**
     * Загрузка в процессе
     * */
    data object LoadingProgress : LoadAddressesViewState()
    /**
     * Данные загружены
     * */
    data class Content(val addresses: List<String>) : LoadAddressesViewState()

    data object AddressSelected : LoadAddressesViewState()
}