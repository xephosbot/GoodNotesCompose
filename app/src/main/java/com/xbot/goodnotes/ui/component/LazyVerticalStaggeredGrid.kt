package com.xbot.goodnotes.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun <T, K> LazyVerticalStaggeredGrid(
    columns: StaggeredGridCells,
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    selectionState: SelectableItemsState<T> = rememberSelectableItemsState(),
    items: ImmutableList<T>,
    key: ((item: T) -> Any)? = null,
    refreshKey: K,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalItemSpacing: Dp = 4.dp,
    horizontalItemSpacing: Dp = 4.dp,
    content: @Composable (item: T, selected: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    var firstLaunchDone by remember { mutableStateOf(false) }

    LaunchedEffect(refreshKey) {
        if (firstLaunchDone) {
            scope.launch {
                state.scrollToItem(0)
            }
        }
        firstLaunchDone = true
    }

    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        verticalItemSpacing = verticalItemSpacing,
        horizontalArrangement = Arrangement.spacedBy(horizontalItemSpacing)
    ) {
        items(items, key) { item ->
            Box(modifier = Modifier.animateItemPlacement()) {
                with(selectionState) {
                    content(item, item.selected)
                }
            }
        }
    }
}
