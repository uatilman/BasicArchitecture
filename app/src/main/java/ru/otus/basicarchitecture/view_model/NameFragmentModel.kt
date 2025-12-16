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
import ru.otus.basicarchitecture.model.LongFiledDto
import ru.otus.basicarchitecture.model.StringFiledDto
import ru.otus.basicarchitecture.model.ValidationEvent
import ru.otus.basicarchitecture.use_case.FieldValidationUseCase

@HiltViewModel
class NameFragmentModel @Inject constructor(
    private val fieldValidationUseCase: FieldValidationUseCase
) : ViewModel() {

    private val _nameFlow = MutableStateFlow(StringFiledDto("", false))
    val nameFlow: StateFlow<StringFiledDto> = _nameFlow.asStateFlow()

    private val _surnameFlow = MutableStateFlow(StringFiledDto("", false))
    val surnameFlow: StateFlow<StringFiledDto> = _surnameFlow.asStateFlow()

    private val _birthDateFlow = MutableStateFlow(LongFiledDto(0, false))
    val birthDateFlow: StateFlow<LongFiledDto> = _birthDateFlow.asStateFlow()

    private val _validationEvent = MutableSharedFlow<ValidationEvent>()
    val validationEvent: SharedFlow<ValidationEvent> = _validationEvent

    fun setName(name: String) {
        if (fieldValidationUseCase.isNameInvalid(name)) {
            _nameFlow.value = StringFiledDto(name, false)
            sendEvent(ValidationEvent.InvalidName)
        } else {
            sendEvent(ValidationEvent.ValidName)
            _nameFlow.value = StringFiledDto(name, true)
        }
    }

    fun setSurname(surname: String) {
        if (fieldValidationUseCase.isSurnameInvalid(surname)) {
            _surnameFlow.value = StringFiledDto(surname, false)
            sendEvent(ValidationEvent.InvalidSurname)
        } else {
            _surnameFlow.value = StringFiledDto(surname, true)
            sendEvent(ValidationEvent.ValidSurname)
        }
    }

    fun setBirthDate(date: Long) {
        if (fieldValidationUseCase.isAgeInvalid(date)) {
            _birthDateFlow.value = LongFiledDto(date, false)
            sendEvent(ValidationEvent.InvalidAge)
        } else {
            _birthDateFlow.value = LongFiledDto(date, true)
            sendEvent(ValidationEvent.ValidAge)
        }
    }

    private fun sendEvent(event: ValidationEvent) = viewModelScope.launch {
        _validationEvent.emit(event)
    }
}