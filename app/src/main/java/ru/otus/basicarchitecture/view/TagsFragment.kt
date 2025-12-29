package ru.otus.basicarchitecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import ru.otus.basicarchitecture.databinding.FragmentTagsBinding
import ru.otus.basicarchitecture.model.Interest
import ru.otus.basicarchitecture.view_model.TagsViewModel
import ru.otus.basicarchitecture.view_model.WizardCache

@AndroidEntryPoint
class TagsFragment : Fragment(), ItemListener {


    private val binding = FragmentBindingDelegate<FragmentTagsBinding>(this)

    private val viewModel: TagsViewModel by viewModels()

    private val tagAdapter: TagAdapter by lazy { TagAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentTagsBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        collectToTagsFlow()
        setupClickListeners()
    }

    private fun collectToTagsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tagsFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::submitToList)
        }
    }

    private fun submitToList(interestList: Set<Interest>) {
        tagAdapter.submitList(interestList.map { TagItem(it) })
    }

    private fun setupRecyclerView() = binding.withBinding {
        groupTags.addItemDecoration(
            DividerItemDecoration(
                this@TagsFragment.requireActivity(),
                LinearLayout.VERTICAL
            )
        )
        groupTags.adapter = tagAdapter
    }

    override fun onItemClick(interest: Interest, isSelected: Boolean) {
        viewModel.onTagSelected(interest, isSelected)
    }

    private fun setupClickListeners() =
        binding.withBinding { toResultNextButton.setOnClickListener(::toTagsButtonClickListener) }

    private fun toTagsButtonClickListener(view: View) {
        findNavController().navigate(TagsFragmentDirections.actionTagsFragmentToResultFragment())
    }

}

interface ItemListener {
    fun onItemClick(interest: Interest, isSelected: Boolean)
}