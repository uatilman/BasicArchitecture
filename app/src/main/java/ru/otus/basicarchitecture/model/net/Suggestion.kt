package ru.otus.basicarchitecture.model.net

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Suggestion (
    val value: String,
    @SerialName("unrestricted_value")
    val unrestrictedValue: String
)

@Serializable
data class Suggestions(
    val suggestions: List<Suggestion>
)