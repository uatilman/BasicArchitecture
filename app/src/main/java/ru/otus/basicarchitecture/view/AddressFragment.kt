package ru.otus.basicarchitecture.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.view_model.AddressFragmentModel


@AndroidEntryPoint
class AddressFragment : Fragment(), AddressItemListener {

    private var binding = FragmentBindingDelegate<FragmentAddressBinding>(this)

    // TODO: добавить loader на время загрузки вариантов RecyclerView
    private val viewModel by viewModels<AddressFragmentModel>()

    private val adapter: AddressAdapter by lazy { AddressAdapter(this) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentAddressBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextWatchers()
        setupClickListeners()
        setupRecyclerView()
        collectToAddressVariantsFlow()
        collectToSelectedFlow()
    }

    private fun setupRecyclerView() = binding.withBinding { addressVariants.adapter = adapter }

    private fun collectToSelectedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addressFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { address ->
                    binding.withBinding {
                        addressMenu.editText?.let {
                            it.setText(address)
                            it.setSelection(address.length) //сдвинуть курсор в конец
                        }
                    }
                }
        }
    }

    private fun collectToAddressVariantsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addressVariantsFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::submitToAddressVariantsList)
        }
    }

    private fun submitToAddressVariantsList(addresses: List<String>) {
        adapter.submitList(addresses.map { AddressItem(it) })
    }


    private fun setupTextWatchers() {
        binding.withBinding {
            addressMenu.editText?.doOnTextChanged { text, start, before, count ->
                if (count > 3) {
                    viewModel.loadAddressVariants(text.toString())
                    addressVariants.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupClickListeners() =
        binding.withBinding { toTagsNextButton.setOnClickListener(::toTagsButtonClickListener) }

    private fun toTagsButtonClickListener(view: View) {
        findNavController().navigate(AddressFragmentDirections.actionAddressFragmentToTagsFragment())
    }

    override fun onItemClick(address: String) {
        binding.withBinding {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.clearVariants()
                if (viewModel.updateAddress(address)) {
                    viewModel.loadAddressVariants(address)
                } else {
                    hideKeyboard()
                    addressVariants.visibility = View.GONE
                    toTagsNextButton.requestFocus()
                }
            }
        }
    }

    private fun FragmentAddressBinding.hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(addressMenu.editText?.windowToken, 0)
    }
}


interface AddressItemListener {
    fun onItemClick(address: String)
}
