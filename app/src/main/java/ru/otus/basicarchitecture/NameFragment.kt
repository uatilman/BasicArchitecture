package ru.otus.basicarchitecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import ru.otus.basicarchitecture.databinding.FragmentNameBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NameFragment : Fragment() {

    private var _binding: FragmentNameBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val datePickerBuilder = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(dataPickerTitle)

            if (birthday.text.toString().isNotEmpty()) {
                try {
                    dateFormat
                        .parse(birthday.text.toString())
                        ?.let {
                            datePickerBuilder.setSelection(it.time)
                        }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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
                    //
                }
            }


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}