package ru.otus.basicarchitecture.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.otus.basicarchitecture.databinding.FragmentResultBinding
import ru.otus.basicarchitecture.model.Interest
import ru.otus.basicarchitecture.view_model.WizardCache
import java.util.Date


@AndroidEntryPoint
class ResultFragment : Fragment(), ItemListener {

    @Inject
    lateinit var dataCache: WizardCache

    private val binding = FragmentBindingDelegate<FragmentResultBinding>(this)

    private val tagAdapter: TagAdapter by lazy { TagAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(container, FragmentResultBinding::inflate)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        tagAdapter.submitList(dataCache.userData.tags.map { TagItem(it) })
        binding.withBinding {
            val u = dataCache.userData
            nameResult.text = u.name
            surnameResult.text = u.name
            birthdayResult.text = dateFormat.format(Date(u.birthDate))
            addressResult.text = u.address.toString()
        }
    }

    private fun setupRecyclerView() = binding.withBinding {
        groupTagsResult.addItemDecoration(
            DividerItemDecoration(
                this@ResultFragment.requireActivity(),
                LinearLayout.VERTICAL
            )
        )
        groupTagsResult.adapter = tagAdapter
    }

    override fun onItemClick(interest: Interest, isSelected: Boolean) {
        // do nothing
    }
}