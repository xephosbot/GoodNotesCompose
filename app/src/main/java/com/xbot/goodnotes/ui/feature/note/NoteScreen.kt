package com.xbot.goodnotes.ui.feature.note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.goodnotes.R
import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import com.xbot.goodnotes.ui.component.LargeTopAppBarWithSelectionMode
import com.xbot.goodnotes.ui.component.LazyVerticalStaggeredGrid
import com.xbot.goodnotes.ui.component.Scaffold
import com.xbot.goodnotes.ui.component.SelectableItemsState
import com.xbot.goodnotes.ui.component.rememberSelectableItemsState
import com.xbot.goodnotes.ui.plus
import com.xbot.ui.component.AnimatedFloatingActionButton
import com.xbot.ui.component.NoteCard
import com.xbot.ui.component.NoteCardDefaults
import com.xbot.ui.component.SelectableChip
import com.xbot.ui.component.SelectableChipBadge
import com.xbot.ui.component.ShapedIconToggleButton
import com.xbot.ui.component.isScrollingUp
import com.xbot.ui.theme.NoteColors
import com.xbot.ui.theme.harmonized
import kotlinx.collections.immutable.ImmutableList

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = hiltViewModel(),
    navigate: (Long) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        navigate = navigate
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun NoteScreenContent(
    modifier: Modifier = Modifier,
    state: NoteScreenState,
    onEvent: (NoteScreenEvent) -> Unit,
    navigate: (Long) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val selectionState = rememberSelectableItemsState<Note>()
    val lazyGridState = rememberLazyStaggeredGridState()

    BackHandler(enabled = selectionState.inSelectionMode) {
        selectionState.clear()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NoteScreenTopAppBar(
                selectionState = selectionState,
                onSettingsClick = {
                    //TODO: navigate to settings screen
                },
                onDeleteClick = {
                    onEvent(NoteScreenEvent.DeleteNotes(selectionState.selectedItems))
                    selectionState.clear()
                },
                onMoreClick = {
                    //TODO: drop down menu with multiple functions
                },
                onClearClick = {
                    selectionState.clear()
                },
                scrollBehavior = scrollBehavior
            ) {
                FolderLazyRow(
                    items = state.foldersList,
                    isFolderSelected = { it.id == state.currentFolderId },
                    onFolderClick = { folder ->
                        onEvent(NoteScreenEvent.OpenFolder(folder.id))
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        floatingActionButton = {
            AnimatedFloatingActionButton(
                onClick = { /*TODO*/ },
                visible = lazyGridState.isScrollingUp().value && !selectionState.inSelectionMode
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        }
    ) { innerPadding ->
        //TODO: Layout switching parameter
        Crossfade(targetState = true) { isGridLayout ->
            when(isGridLayout) {
                true -> {
                    NoteLazyGrid(
                        state = lazyGridState,
                        selectionState = selectionState,
                        items = state.notesList,
                        refreshKey = state.currentFolderId,
                        contentPadding = innerPadding + PaddingValues(horizontal = 4.dp),
                        onCLickNoteCard = { note ->
                            navigate(note.id)
                        },
                        onFavoriteClick = { note ->
                            onEvent(NoteScreenEvent.UpdateNote(note, !note.isFavorite))
                        }
                    )
                }
                else -> {
                    NoteLazyColumn()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopAppBar(
    modifier: Modifier = Modifier,
    selectionState: SelectableItemsState<Note>,
    onSettingsClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoreClick: () -> Unit,
    onClearClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable ColumnScope.() -> Unit
) {
    LargeTopAppBarWithSelectionMode(
        modifier = modifier,
        selected = selectionState.inSelectionMode,
        title = { selected ->
            when(selected) {
                true -> "Notes:\n${selectionState.selectedCount}"
                else -> stringResource(R.string.my_notes_title)
            }
        },
        navigationIcon = { selected ->
            if (selected) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = ""
                    )
                }
            }
        },
        actions = { selected ->
            when(selected) {
                true -> {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = ""
                        )
                    }
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = ""
                        )
                    }
                }
                else -> {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = ""
                        )
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior,
        content = content
    )
}

@Composable
private fun FolderLazyRow(
    modifier: Modifier = Modifier,
    items: ImmutableList<Folder>,
    isFolderSelected: (Folder) -> Boolean,
    onFolderClick: (Folder) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { folder ->
            SelectableChip(
                selected = isFolderSelected(folder),
                onClick = { onFolderClick(folder) },
                label = { Text(text = folder.name) },
                leadingIcon = { SelectableChipBadge(text = folder.noteCount.toString()) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteLazyGrid(
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState,
    selectionState: SelectableItemsState<Note>,
    items: ImmutableList<Note>,
    refreshKey: Long,
    contentPadding: PaddingValues,
    onCLickNoteCard: (Note) -> Unit,
    onFavoriteClick: (Note) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(180.dp),
        state = state,
        selectionState = selectionState,
        items = items,
        key = { it.id },
        refreshKey = refreshKey,
        contentPadding = contentPadding
    ) { note, selected ->
        NoteCard(
            note = note,
            selected = selected,
            onClick = {
                when(selectionState.inSelectionMode) {
                    true -> selectionState.updateItem(note)
                    else -> onCLickNoteCard(note)
                }
            },
            onLongClick = {
                selectionState.updateItem(note)
            },
            onFavoriteClick = {
                when(selectionState.inSelectionMode) {
                    true -> selectionState.updateItem(note)
                    else -> onFavoriteClick(note)
                }
            }
        )
    }
}

@Composable
private fun NoteLazyColumn(
    modifier: Modifier = Modifier
) {
    //TODO: Lazy column layout variant
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    selected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    NoteCard(
        modifier = modifier,
        selected = selected,
        colors = NoteCardDefaults.noteCardColors(
            containerColor = NoteColors[note.colorId].harmonized,
            contentColor = Color.Black
        ),
        headlineContent = {
            Text(
                text = note.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            ShapedIconToggleButton(
                checked = selected,
                onCheckedChange = { onFavoriteClick() }
            ) {
                Icon(
                    imageVector = if (!selected) {
                        if (!note.isFavorite) {
                            Icons.Outlined.FavoriteBorder
                        } else {
                            Icons.Filled.Favorite
                        }
                    } else {
                        Icons.Filled.Done
                    },
                    contentDescription = ""
                )
            }
        },
        supportingContent = { Text(text = note.dateTime) },
        bodyContent = { Text(text = note.content) },
        onClick = { onClick() },
        onLongClick = { onLongClick() }
    )
}
