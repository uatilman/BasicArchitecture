package ru.otus.basicarchitecture.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.databinding.FragmentAddressBinding
import ru.otus.basicarchitecture.view_model.AddressFragmentModel


@AndroidEntryPoint
class AddressFragment : Fragment(), AddressItemListener {

    private var binding = FragmentBindingDelegate<FragmentAddressBinding>(this)

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
        collectToAddressVariantsStateFlow()
        collectToSelectedFlow()
    }

    private fun setupRecyclerView() = binding.withBinding { addressVariants.adapter = adapter }

    private fun collectToSelectedFlow() {
        viewModel.addressFlow.onEach {
            viewModel.addressFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::onAddressSelected)
        }.launchIn(lifecycleScope)
    }

    private fun onAddressSelected(address: String) {
        binding.withBinding {
            addressMenu.editText?.let {
                it.setText(address)
                it.setSelection(address.length) //сдвинуть курсор в конец
            }
        }
    }

    private fun collectToAddressVariantsStateFlow() {
        viewModel.state.onEach {
            binding.withBinding {
                when (it) {
                    is LoadAddressesViewState.Content -> {
                        addressVariants.isVisible = true
                        loadIndicator.isVisible = false
                        errorMessage.isVisible = false
                        adapter.submitList(it.addresses.map { adr -> AddressItem(adr) })
                    }

                    is LoadAddressesViewState.LoadAddresses -> {
                        addressVariants.isVisible = false
                        loadIndicator.isVisible = false
                        errorMessage.isVisible = it.error != null
                    }

                    LoadAddressesViewState.LoadingProgress -> {
                        addressVariants.isVisible = false
                        loadIndicator.isVisible = true
                        errorMessage.isVisible = false
                    }

                    LoadAddressesViewState.AddressSelected -> {
                        addressVariants.isVisible = false
                        loadIndicator.isVisible = false
                        errorMessage.isVisible = false
                        hideKeyboard()
                        toTagsNextButton.requestFocus()
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun setupTextWatchers() {
        binding.withBinding {
            addressMenu.editText?.doOnTextChanged { text, start, before, count ->
                if (count > 3) {
                    viewModel.loadAddressVariants(text.toString())
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
                    viewModel.addressSelected()
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
