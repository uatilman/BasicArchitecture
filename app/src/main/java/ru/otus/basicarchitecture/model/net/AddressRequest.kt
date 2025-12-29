package ru.otus.basicarchitecture.model.net

import kotlinx.serialization.Serializable

@Serializable
data class AddressRequest(val query: String, val count: Int)