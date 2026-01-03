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
            name.isEmpty() || !pattern.toRegex().matches(name)

        override fun isSurnameInvalid(surname: String) =
            surname.isEmpty() || !pattern.toRegex().matches(surname)

        override fun isAgeInvalid(birthDate: Long?) =
            birthDate == null || birthDate > (System.currentTimeMillis() - EIGHTEEN_YEARS)

        companion object {
            // Валидация: 1–18 символов, начинается с заглавной, далее строчные или разрешённые символы (не подряд, не в конце)
            private const val USERNAME_PATTERN = "^(?=.{1,18}\$)[A-ZА-Я](?:[.'_-](?![.'_-]))?[a-zа-я]*\$"

            private const val EIGHTEEN_YEARS =  (18 * 365.25 * 24 * 60 * 60 * 1000).toLong()

            private val pattern: Pattern =
                Pattern.compile(USERNAME_PATTERN, Pattern.CASE_INSENSITIVE)
        }
    }
}