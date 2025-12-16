package ru.otus.basicarchitecture.view

import android.view.View
import com.google.android.material.textfield.TextInputEditText


fun createValidationOnFocusChangeListener(
    validate: (String) -> Unit
): View.OnFocusChangeListener {
    return View.OnFocusChangeListener { view, hasFocus ->
        val text = (view as? TextInputEditText)?.text?.toString() ?: ""
        if (!hasFocus) {
            validate(text)
        }
    }
}