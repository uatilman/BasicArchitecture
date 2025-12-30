package ru.otus.basicarchitecture.model

data class UserData(
    val name: String,
    val surname: String,
    val birthDate: Long,
    val address: String,
    val tags: Set<Interest>,
)