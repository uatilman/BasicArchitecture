package ru.otus.basicarchitecture.view

import androidx.annotation.LayoutRes
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.model.Interest

data class TagItem(
    val interest: Interest,
    var isSelected: Boolean = false
) : WithLayoutId by TagItem {
    companion object : WithLayoutId {
        @get:LayoutRes
        override val layoutId: Int = R.layout.vh_tag
    }

    val name: String get() = interest.title
}