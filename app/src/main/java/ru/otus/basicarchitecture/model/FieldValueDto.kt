package ru.otus.basicarchitecture.model

interface FieldValueDto<T> {
    val fValue: T
    val isValid: Boolean
}