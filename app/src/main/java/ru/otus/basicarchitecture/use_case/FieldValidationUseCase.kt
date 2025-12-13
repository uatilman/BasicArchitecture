package ru.otus.basicarchitecture.use_case

import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import java.util.regex.Pattern


@ViewModelScoped
interface FieldValidationUseCase {
    fun isNameInvalid(name: String): Boolean
    fun isSurnameInvalid(surname: String): Boolean
    fun isAgeInvalid(birthDate: Long?): Boolean


    class Impl @Inject constructor() : FieldValidationUseCase {
        override fun isNameInvalid(name: String) =
            name.length > 2 && !pattern.toRegex().matches(name)

        override fun isSurnameInvalid(surname: String) =
            surname.length > 2 && !pattern.toRegex().matches(surname)

        override fun isAgeInvalid(birthDate: Long?) =
            birthDate == null || birthDate > eighteenYearsAgo

        companion object {
            private const val USERNAME_PATTERN = "^[A-ZА-Я]([._-](?![._-])|[a-zа-я]){1,18}[a-zа-я]$"
            private val eighteenYearsAgo = System.currentTimeMillis() - (18 * 365.25 * 24 * 60 * 60 * 1000).toLong()

            private val pattern: Pattern =
                Pattern.compile(USERNAME_PATTERN, Pattern.CASE_INSENSITIVE)

        }
    }
}