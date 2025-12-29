package ru.otus.basicarchitecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.view_model.AddressFragmentModel

@AndroidEntryPoint
class AddressFragment : Fragment() {


    private var binding = FragmentBindingDelegate<FragmentAddressBinding>(this)

    private val viewModel by viewModels<AddressFragmentModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentAddressBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextWatchers()
        setupClickListeners()
    }

    private fun setupTextWatchers() {
        binding.withBinding {
            country.onFocusChangeListener =
                createValidationOnFocusChangeListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateAddress(country = it)
                    }
                }
            city.onFocusChangeListener =
                createValidationOnFocusChangeListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateAddress(
                            city = it
                        )
                    }
                }
            street.onFocusChangeListener =
                createValidationOnFocusChangeListener {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.updateAddress(
                            street = it
                        )
                    }
                }
        }
    }

    private fun setupClickListeners() =
        binding.withBinding { toTagsNextButton.setOnClickListener(::toTagsButtonClickListener) }

    private fun toTagsButtonClickListener(view: View) {
        findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToTagsFragment())
    }
}