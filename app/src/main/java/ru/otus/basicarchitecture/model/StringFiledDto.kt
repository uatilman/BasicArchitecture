package ru.otus.basicarchitecture.model

class StringFiledDto(override val fValue: String, override val isValid: Boolean) :
    FieldValueDto<String>