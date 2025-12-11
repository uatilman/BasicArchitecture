package ru.otus.basicarchitecture.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class NameFragmentModel : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _surname = MutableStateFlow("")
    val surname: StateFlow<String> = _surname.asStateFlow()

    private val _birthDate = MutableStateFlow<Long?>(null)
    val birthDate: StateFlow<Long?> = _birthDate.asStateFlow()

    private val _validationEvent = MutableSharedFlow<Event<ValidationEvent>>()
    val validationEvent: SharedFlow<Event<ValidationEvent>> = _validationEvent

    fun setName(name: String) {
        _name.value = name

    }


    fun setSurname(surname: String) {
        _surname.value = surname

    }

    fun setBirthDate(date: Long?) {
        _birthDate.value = date

    }

    fun validateName() {
        if (name.value.length > 2 && !pattern.toRegex().matches(name.value))
            sendEvent(ValidationEvent.InvalidName)
    }

    fun validateSurname() {
        if (surname.value.length > 2 && !pattern.toRegex().matches(surname.value))
            sendEvent(ValidationEvent.InvalidSurname)
    }

    fun validateAge() {
        val birthDate = birthDate.value
        if (birthDate == null || birthDate > eighteenYearsAgo) {
            sendEvent(ValidationEvent.InvalidAge)
        }
    }

    private fun sendEvent(event: ValidationEvent) = viewModelScope.launch {
        _validationEvent.emit(Event(event))
    }

    companion object {
        const val USERNAME_PATTERN = "^[A-ZА-Я]([._-](?![._-])|[a-zа-я]){1,18}[a-zа-я]$"
        val eighteenYearsAgo: Long
            get() = System.currentTimeMillis() - (18 * 365.25 * 24 * 60 * 60 * 1000).toLong()

        var pattern: Pattern =
            Pattern.compile(USERNAME_PATTERN, Pattern.CASE_INSENSITIVE)

    }
}


sealed interface ValidationEvent {
    object InvalidName : ValidationEvent
    object InvalidSurname : ValidationEvent
    object InvalidAge : ValidationEvent
}

open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}