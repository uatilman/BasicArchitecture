package ru.otus.basicarchitecture.model

sealed interface ValidationEvent {
    object InvalidName : ValidationEvent
    object ValidName : ValidationEvent
    object InvalidSurname : ValidationEvent
    object ValidSurname : ValidationEvent
    object InvalidAge : ValidationEvent
    object ValidAge : ValidationEvent
}