package ru.otus.basicarchitecture.view

import androidx.annotation.LayoutRes
import ru.otus.basicarchitecture.R

data class AddressItem(
    val address: String,
) : WithLayoutId by AddressItem {
    companion object : WithLayoutId {
        @get:LayoutRes
        override val layoutId: Int = R.layout.list_item
    }

}