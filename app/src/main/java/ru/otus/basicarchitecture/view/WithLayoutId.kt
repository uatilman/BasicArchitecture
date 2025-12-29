package ru.otus.basicarchitecture.view

import androidx.annotation.LayoutRes

/**
 * Represents an object that has a layout ID.
 */
interface WithLayoutId {
    @get:LayoutRes
    val layoutId: Int
}