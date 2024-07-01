package com.xbot.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun <T> rememberReorderableItemsState(items: List<T>): ReorderableItemsState<T> {
    return rememberSaveable(items, saver = ReorderableItemsState.Saver()) {
        ReorderableItemsState(items)
    }
}

class ReorderableItemsState<T> private constructor(items: List<T>, reorderMode: Boolean) {

    constructor(items: List<T>) : this(items, false)

    private var _list by mutableStateOf(items)
    val list: List<T> get() = _list

    private var _inReorderMode by mutableStateOf(reorderMode)
    val inReorderMode: Boolean get() = _inReorderMode

    fun swapItems(fromIndex: Int, toIndex: Int) {
        _list = _list.toMutableList().apply { add(toIndex, removeAt(fromIndex)) }
    }

    fun setReorderMode(value: Boolean) {
        _inReorderMode = value
    }

    companion object {
        fun <T> Saver(): Saver<ReorderableItemsState<T>, Any> = @Suppress("UNCHECKED_CAST") (Saver(
        save = { state ->
            listOf(state.inReorderMode, state.list)
        },
        restore = { value ->
            val (reorderMode, list) = value as List<*>
            ReorderableItemsState(
                reorderMode = reorderMode as Boolean,
                items = list as List<T>
            )
        }
    ))
    }
}