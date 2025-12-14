package ru.otus.basicarchitecture.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.use_case.FieldValidationUseCase

@HiltViewModel
class NameFragmentModel @Inject constructor(
    private val fieldValidationUseCase: FieldValidationUseCase
) : ViewModel() {

    private val _name = MutableStateFlow(StringFiledDto("", false))
    val name: StateFlow<StringFiledDto> = _name.asStateFlow()

    private val _surname = MutableStateFlow(StringFiledDto("", false))
    val surname: StateFlow<StringFiledDto> = _surname.asStateFlow()

    private val _birthDate = MutableStateFlow(LongFiledDto(0, false))
    val birthDate: StateFlow<LongFiledDto> = _birthDate.asStateFlow()

    private val _validationEvent = MutableSharedFlow<ValidationEvent>()
    val validationEvent: SharedFlow<ValidationEvent> = _validationEvent

    fun setName(name: String) {
        if (fieldValidationUseCase.isNameInvalid(name)) {
            _name.value = StringFiledDto(name, false)
            sendEvent(ValidationEvent.InvalidName)
        } else {
            sendEvent(ValidationEvent.ValidName)
            _name.value = StringFiledDto(name, true)
        }
    }

    fun setSurname(surname: String) {
        if (fieldValidationUseCase.isSurnameInvalid(surname)) {
            _surname.value = StringFiledDto(surname, false)
            sendEvent(ValidationEvent.InvalidSurname)
        } else {
            _surname.value = StringFiledDto(surname, true)
            sendEvent(ValidationEvent.ValidSurname)
        }
    }

    fun setBirthDate(date: Long) {
        if (fieldValidationUseCase.isAgeInvalid(date)) {
            _birthDate.value = LongFiledDto(date, false)
            sendEvent(ValidationEvent.InvalidAge)
        } else {
            _birthDate.value = LongFiledDto(date, true)
            sendEvent(ValidationEvent.ValidAge)
        }
    }

    private fun sendEvent(event: ValidationEvent) = viewModelScope.launch {
        _validationEvent.emit(event)
    }
}

interface FieldValueDto<T> {
    val fValue: T
    val isValid: Boolean
}

class StringFiledDto(override val fValue: String, override val isValid: Boolean) :
    FieldValueDto<String>

class LongFiledDto(override val fValue: Long, override val isValid: Boolean) : FieldValueDto<Long?>

sealed interface ValidationEvent {
    object InvalidName : ValidationEvent
    object ValidName : ValidationEvent
    object InvalidSurname : ValidationEvent
    object ValidSurname : ValidationEvent
    object InvalidAge : ValidationEvent
    object ValidAge : ValidationEvent
}