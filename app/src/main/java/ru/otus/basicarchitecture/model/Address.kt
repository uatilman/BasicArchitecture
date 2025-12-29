package ru.otus.basicarchitecture.model

data class Address(
    var country: String = "",
    var city: String = "",
    var street: String = ""
) {
    override fun toString() = "$country, $city, $street"
}