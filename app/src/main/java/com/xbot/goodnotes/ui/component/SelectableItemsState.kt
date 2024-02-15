package com.xbot.goodnotes.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun <T> rememberSelectableItemsState(): SelectableItemsState<T> {
    return remember {
        SelectableItemsState()
    }
}

class SelectableItemsState<T> {

    private val _selectedItems = mutableStateListOf<T>()

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
}