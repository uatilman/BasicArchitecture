package ru.otus.basicarchitecture.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.otus.basicarchitecture.model.Interest
import ru.otus.basicarchitecture.service.InterestsRepository

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val interestsRepository: InterestsRepository,
    private val dataCache: WizardCache
) : ViewModel() {

    private val _selectedTagsFlow = MutableStateFlow<MutableSet<Interest>>(mutableSetOf())
    val selectedTagsFlow = _selectedTagsFlow.asStateFlow()

    val tagsFlow: Flow<Set<Interest>> = interestsRepository.getInterests()

    init {
        selectedTagsFlow.onEach {
            dataCache.tags = it
        }.launchIn(viewModelScope)
    }

    fun onTagSelected(id: Interest, isSelected: Boolean) {
        val currentSet = _selectedTagsFlow.value
        if (isSelected) {
            currentSet.add(id)
        } else {
            currentSet.remove(id)
        }
        _selectedTagsFlow.value = currentSet
    }
}