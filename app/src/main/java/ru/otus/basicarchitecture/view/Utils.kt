package ru.otus.basicarchitecture.view

import android.view.View
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


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

val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}
