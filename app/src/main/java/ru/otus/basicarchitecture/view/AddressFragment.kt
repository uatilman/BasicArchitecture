package ru.otus.basicarchitecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.view_model.AddressFragmentModel

@AndroidEntryPoint
class AddressFragment : Fragment() {


    private var binding = FragmentBindingDelegate<FragmentAddressBinding>(this)

    private val viewModel by viewModels<AddressFragmentModel>()

    private lateinit var adapter: ArrayAdapter<String>
    lateinit var addresses: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentAddressBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextWatchers()
        setupClickListeners()

        addresses = mutableListOf()
        adapter = ArrayAdapter(requireContext(), R.layout.list_item, addresses)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addressVariantsFlow.collect {
                if (!adapter.isEmpty) adapter.clear()
                addresses = it
                adapter.addAll(addresses)
                adapter.notifyDataSetChanged()
            }
        }
        binding.withBinding {
            (addressMenu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }

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
            addressMenu.editText?.doOnTextChanged { text, _, _, count ->
                if (count > 3) viewModel.loadAddressVariants(text.toString())
            }


            // TODO: change to recycler view
            (addressMenu.editText as? AutoCompleteTextView)?.onItemClickListener =
                AdapterView.OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    val selectedAddress: String? = adapter.getItem(position)
                    addressAutoCompleteText.setText(selectedAddress)
                }

        }
    }

    private fun setupClickListeners() =
        binding.withBinding { toTagsNextButton.setOnClickListener(::toTagsButtonClickListener) }

    private fun toTagsButtonClickListener(view: View) {
        findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToTagsFragment())
    }
}


