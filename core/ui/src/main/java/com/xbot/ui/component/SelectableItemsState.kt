package com.xbot.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun <T> rememberSelectableItemsState(): SelectableItemsState<T> =
    rememberSaveable(saver = SelectableItemsState.Saver()) {
        SelectableItemsState()
    }

class SelectableItemsState<T> private constructor(initialItems: List<T>) {

    constructor() : this(emptyList())

    private val _selectedItems = mutableStateListOf<T>()

    init {
        _selectedItems.addAll(initialItems)
    }

    val inSelectionMode: Boolean
        get() = derivedStateOf { _selectedItems.isNotEmpty() }.value

    val selectedItems: List<T>
        get() = _selectedItems.toList()

    val selectedCount: Int
        get() = _selectedItems.count()

    val T.selected: Boolean
        get() = _selectedItems.contains(this)

    fun updateItem(item: T) {
        when (item.selected) {
            true -> _selectedItems.remove(item)
            else -> _selectedItems.add(item)
        }
    }

    fun clear() {
        _selectedItems.clear()
    }

    companion object {
        fun <T> Saver(): Saver<SelectableItemsState<T>, Any> =
            listSaver(
                save = { state -> state.selectedItems.toList() },
                restore = { selectedItems -> SelectableItemsState(selectedItems) }
            )
    }
}