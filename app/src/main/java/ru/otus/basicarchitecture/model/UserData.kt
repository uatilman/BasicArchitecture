package ru.otus.basicarchitecture.model

data class UserData(
    val name: String,
    val surname: String,
    val birthDate: Long,
    val country: String,
    val city: String,
    val address: String
)