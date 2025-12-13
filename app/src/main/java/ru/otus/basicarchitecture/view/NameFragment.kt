package ru.otus.basicarchitecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentNameBinding
import ru.otus.basicarchitecture.view_model.NameFragmentModel
import ru.otus.basicarchitecture.view_model.ValidationEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime


@AndroidEntryPoint
class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<NameFragmentModel>()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val dataPickerTitle by lazy { getString(R.string.data_picker_title) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalTime::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataPicker()
        setupTextWatchers()
        setupClickListeners()
        setupObservers()
    }

    private fun toAddressButtonClickListener(view: View) {
        findNavController().navigate(NameFragmentDirections.actionNameFragmentToAddressFragment())
    }

    private fun setupTextWatchers() {
        with(binding) {
            name.onFocusChangeListener =
                createValidationOnFocusChangeListener { viewModel.setName(it) }
            surname.onFocusChangeListener =
                createValidationOnFocusChangeListener { viewModel.setSurname(it) }
        }
    }

    private fun createValidationOnFocusChangeListener(
        validate: (String) -> Unit
    ): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { view, hasFocus ->
            val text = (view as? TextInputEditText)?.text?.toString() ?: ""
            if (!hasFocus) {
                validate(text)
            }
        }
    }

    private fun setupClickListeners() {
        binding.toAddressNextButton.setOnClickListener(::toAddressButtonClickListener)
    }

    private fun setupObservers() {
        // Наблюдение за событиями валидации
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.validationEvent.collect { event ->
                when (event) {
                    ValidationEvent.InvalidName -> doInvalidName()
                    ValidationEvent.InvalidSurname -> doInvalidSurname()
                    ValidationEvent.InvalidAge -> doInvalidAge()

                    ValidationEvent.ValidAge,
                    ValidationEvent.ValidName,
                    ValidationEvent.ValidSurname -> doSomeValid()

                }
            }
        }
    }

    private fun doSomeValid() {
        if (
            viewModel.name.value.isValid
            && viewModel.surname.value.isValid
            && viewModel.birthDate.value.isValid
        )
            enableNextButton()
    }

    private fun enableNextButton() {
        with(binding) {
            toAddressNextButton.isEnabled = true
            toAddressNextButton.isClickable = true
        }
    }

    private fun disableNextButton() {
        with(binding) {
            toAddressNextButton.isEnabled = false
            toAddressNextButton.isClickable = false
        }
    }

    private fun doInvalidName() {
        disableNextButton()
        Toast.makeText(context, getString(R.string.invalid_name), Toast.LENGTH_SHORT).show()
    }

    private fun doInvalidSurname() {
        disableNextButton()
        Toast.makeText(context, getString(R.string.invalid_surname), Toast.LENGTH_SHORT).show()
    }

    private fun doInvalidAge() {
        disableNextButton()
        Toast.makeText(context, getString(R.string.invalid_age), Toast.LENGTH_SHORT).show()
    }

    private fun setDataPicker() {
        with(binding) {
            val datePickerBuilder = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(dataPickerTitle)
            val textData = birthday.text.toString()
            if (textData.isNotEmpty()) {
                runCatching {
                    parseTextData(textData)?.let { datePickerBuilder.setSelection(it.time) }
                }.getOrElse { it.printStackTrace() }
            }
            val datePicker = datePickerBuilder.build()
            birthday.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    datePicker.addOnPositiveButtonClickListener { selection: Long ->
                        birthday.setText(dateFormat.format(Date(selection)))
                    }
                    datePicker.addOnNegativeButtonClickListener {
                        birthday.clearFocus()
                    }
                    datePicker.addOnDismissListener {
                        birthday.clearFocus()
                    }
                    datePicker.show(childFragmentManager, "birthday_date")

                } else {
                    (v as? TextInputEditText)
                        ?.text?.toString()
                        ?.let { viewModel.setBirthDate(parseTextData(it)?.time) }
                        ?: viewModel.setBirthDate(null)

                }
            }


        }
    }

    private fun parseTextData(textData: String): Date? = runCatching {
        dateFormat.parse(textData)
    }.getOrNull()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}