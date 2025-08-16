package com.xbot.goodnotes.ui.feature.note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.xbot.common.Constants
import com.xbot.goodnotes.R
import com.xbot.goodnotes.convertToDateTime
import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import com.xbot.goodnotes.navigation.LocalNavSharedElementScope
import com.xbot.goodnotes.navigation.NoteSharedElementKey
import com.xbot.goodnotes.navigation.sharedBoundsRevealWithShapeMorph
import com.xbot.goodnotes.ui.plus
import com.xbot.ui.component.AnimatedFloatingActionButton
import com.xbot.ui.component.DraggableItem
import com.xbot.ui.component.DropdownMenu
import com.xbot.ui.component.FilledShapedIconButton
import com.xbot.ui.component.LargeTopAppBarWithSelectionMode
import com.xbot.ui.component.LazyVerticalStaggeredGridWithSelection
import com.xbot.ui.component.NoteCard
import com.xbot.ui.component.NoteCardDefaults
import com.xbot.ui.component.ReorderableItemsState
import com.xbot.ui.component.Scaffold
import com.xbot.ui.component.SelectableChip
import com.xbot.ui.component.SelectableChipBadge
import com.xbot.ui.component.SelectableItemsState
import com.xbot.ui.component.ShapedIconButton
import com.xbot.ui.component.ShapedIconToggleButton
import com.xbot.ui.component.dragContainer
import com.xbot.ui.component.isScrollingUp
import com.xbot.ui.component.rememberDragDropState
import com.xbot.ui.component.rememberReorderableItemsState
import com.xbot.ui.component.rememberSelectableItemsState
import com.xbot.ui.icon.Icons
import com.xbot.ui.theme.harmonized
import com.xbot.ui.theme.noteColors
import kotlinx.collections.immutable.ImmutableList

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = hiltViewModel(),
    navigateToDetails: (Long) -> Unit,
    navigateToSettings: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteScreenContent(
        state = state,
        onAction = viewModel::onAction,
        navigateToDetails = navigateToDetails,
        navigateToSettings = navigateToSettings
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
private fun NoteScreenContent(
    modifier: Modifier = Modifier,
    state: NoteScreenState,
    onAction: (NoteScreenAction) -> Unit,
    navigateToDetails: (Long) -> Unit,
    navigateToSettings: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val selectionState = rememberSelectableItemsState<Note>()
    val reorderState = rememberReorderableItemsState(state.foldersList)
    val lazyGridState = rememberLazyStaggeredGridState()
    var showAddNewFolderDialog by remember { mutableStateOf(false) }
    var showChangeFolderBottomSheet by remember { mutableStateOf(false) }

    BackHandler(selectionState.inSelectionMode) {
        selectionState.clear()
    }

    BackHandler(reorderState.inReorderMode) {
        onAction(NoteScreenAction.UpdateFolders(reorderState.list))
        reorderState.setReorderMode(false)
    }

    val sharedTransitionScope = LocalNavSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    with(sharedTransitionScope) {
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                NoteScreenTopAppBar(
                    selectionState = selectionState,
                    onSettingsClick = {
                        navigateToSettings()
                    },
                    onDeleteClick = {
                        onAction(NoteScreenAction.DeleteNotes(selectionState.selectedItems))
                        selectionState.clear()
                    },
                    onChangeFolderClick = {
                        onAction(NoteScreenAction.UpdateRelatedFolders(selectionState.selectedItems))
                        if (state.foldersList.isNotEmpty()) showChangeFolderBottomSheet = true
                    },
                    onClearClick = {
                        selectionState.clear()
                    },
                    scrollBehavior = scrollBehavior
                ) {
                    FolderLazyRow(
                        reorderState = reorderState,
                        noteCount = state.noteCount,
                        isFolderSelected = { it == state.currentFolderId },
                        onFolderClick = { folderId ->
                            onAction(NoteScreenAction.OpenFolder(folderId))
                        },
                        onAddNewFolderClick = {
                            showAddNewFolderDialog = true
                        },
                        onDeleteFolderClick = { folder ->
                            onAction(NoteScreenAction.DeleteFolder(folder))
                        },
                        onRenameFolderClick = { folder ->
                            //TODO: Folder rename dialog
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            },
            floatingActionButton = {
                AnimatedFloatingActionButton(
                    modifier = Modifier
                        .sharedBoundsRevealWithShapeMorph(
                            sharedContentState = rememberSharedContentState(NoteSharedElementKey(Constants.NEW_NOTE_ID)),
                            animatedVisibilityScope = animatedVisibilityScope,
                            targetShapeCornerRadius = 0.dp,
                            restingShapeCornerRadius = NoteCardDefaults.ShapeCornerRadius,
                            keepChildrenSizePlacement = false,
                        ),
                    onClick = { navigateToDetails(Constants.NEW_NOTE_ID) },
                    visible = lazyGridState.isScrollingUp().value && !selectionState.inSelectionMode
                ) {
                    Icon(
                        imageVector = Icons.Add,
                        contentDescription = ""
                    )
                }
            }
        ) { innerPadding ->
            NoteLazyGrid(
                state = lazyGridState,
                selectionState = selectionState,
                items = state.notesList,
                refreshKey = state.currentFolderId,
                contentPadding = innerPadding + PaddingValues(horizontal = 4.dp),
                onCLickNoteCard = { note ->
                    navigateToDetails(note.id)
                },
                onFavoriteClick = { note ->
                    onAction(NoteScreenAction.UpdateNote(note, !note.isFavorite))
                }
            )
            if (state.notesList.isEmpty()) {
                EmptyScreenPlaceHolder()
            }
        }
    }

    if (showAddNewFolderDialog) {
        AddNewFolderDialog(
            onDismissRequest = {
                showAddNewFolderDialog = false
            },
            onConfirmation = { folderName ->
                onAction(NoteScreenAction.AddFolder(folderName))
                showAddNewFolderDialog = false
            }
        )
    }

    if (showChangeFolderBottomSheet) {
        ChangeFolderBottomSheet(
            items = state.foldersList,
            isFolderChecked = { folder ->
                state.relatedFolders.contains(folder)
            },
            onFolderCheckedChange = { folder, checked ->
                onAction(
                    NoteScreenAction.ChangeFolderForNotes(
                        notes = selectionState.selectedItems,
                        folder = folder,
                        value = checked
                    )
                )
            },
            onDismissRequest = {
                showChangeFolderBottomSheet = false
            }
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NoteScreenTopAppBar(
    modifier: Modifier = Modifier,
    selectionState: SelectableItemsState<Note>,
    onSettingsClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChangeFolderClick: () -> Unit,
    onClearClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable ColumnScope.() -> Unit
) {
    var showDropDownMenu by remember { mutableStateOf(false) }

    LargeTopAppBarWithSelectionMode(
        modifier = modifier,
        selected = selectionState.inSelectionMode,
        title = { selected ->
            when (selected) {
                true -> stringResource(R.string.notes_count_title, selectionState.selectedCount)
                else -> stringResource(R.string.my_notes_title)
            }
        },
        navigationIcon = { selected ->
            if (selected) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        imageVector = Icons.Clear,
                        contentDescription = ""
                    )
                }
            }
        },
        actions = { selected ->
            when (selected) {
                true -> {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Delete,
                            contentDescription = ""
                        )
                    }
                    Box {
                        IconButton(onClick = { showDropDownMenu = true }) {
                            Icon(
                                imageVector = Icons.MoreVert,
                                contentDescription = ""
                            )
                        }
                        DropdownMenu(
                            expanded = showDropDownMenu,
                            onDismissRequest = {
                                showDropDownMenu = false
                            }
                        ) {
                            //TODO: More options to choose
                            ShapedIconButton(
                                onClick = {
                                    onChangeFolderClick()
                                    showDropDownMenu = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AddToFolder,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }

                else -> {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Settings,
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
    reorderState: ReorderableItemsState<Folder>,
    noteCount: Int,
    isFolderSelected: (Long) -> Boolean,
    onFolderClick: (Long) -> Unit,
    onAddNewFolderClick: () -> Unit,
    onDeleteFolderClick: (Folder) -> Unit,
    onRenameFolderClick: (Folder) -> Unit
) {
    var currentFolderContextMenu by remember { mutableStateOf<Long?>(null) }
    val listState = rememberLazyListState()
    val dragDropState = rememberDragDropState(listState, reorderState) { fromIndex, toIndex ->
        reorderState.swapItems(fromIndex, toIndex)
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .dragContainer(dragDropState),
        state = listState,
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!reorderState.inReorderMode) {
            item(
                key = Constants.DEFAULT_FOLDER_ID
            ) {
                SelectableChip(
                    modifier = Modifier.animateItem(),
                    selected = isFolderSelected(Constants.DEFAULT_FOLDER_ID),
                    onClick = { onFolderClick(Constants.DEFAULT_FOLDER_ID) },
                    label = { Text(text = stringResource(R.string.folder_all_title)) },
                    leadingIcon = { SelectableChipBadge(text = noteCount.toString()) }
                )
            }

            item(
                key = Constants.FAVORITE_FOLDER_ID
            ) {
                SelectableChip(
                    modifier = Modifier.animateItem(),
                    selected = isFolderSelected(Constants.FAVORITE_FOLDER_ID),
                    onClick = { onFolderClick(Constants.FAVORITE_FOLDER_ID) },
                    label = { Text(text = stringResource(R.string.folder_favorite_title)) },
                    leadingIcon = { SelectableChipBadge(text = noteCount.toString()) }
                )
            }
        }

        itemsIndexed(
            items = reorderState.list,
            key = { _, item -> item.id }
        ) { index, folder ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index
            ) { isDragging ->
                Box {
                    SelectableChip(
                        selected = isFolderSelected(folder.id) && !reorderState.inReorderMode,
                        onClick = { onFolderClick(folder.id) },
                        onLongClick = { currentFolderContextMenu = folder.id },
                        label = { Text(text = folder.name) },
                        leadingIcon = { SelectableChipBadge(text = noteCount.toString()) },
                        enabled = !reorderState.inReorderMode
                    )

                    DropdownMenu(
                        expanded = currentFolderContextMenu == folder.id,
                        onDismissRequest = { currentFolderContextMenu = null }
                    ) {
                        ShapedIconButton(
                            onClick = {
                                onDeleteFolderClick(folder)
                                currentFolderContextMenu = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Delete,
                                contentDescription = ""
                            )
                        }
                        ShapedIconButton(
                            onClick = {
                                onRenameFolderClick(folder)
                                currentFolderContextMenu = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Edit,
                                contentDescription = ""
                            )
                        }
                        ShapedIconButton(
                            onClick = {
                                reorderState.setReorderMode(true)
                                currentFolderContextMenu = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Reorder,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }
        }

        if (!reorderState.inReorderMode) {
            item(
                key = Constants.NEW_NOTE_ID
            ) {
                FilledShapedIconButton(
                    modifier = Modifier.animateItem(),
                    onClick = onAddNewFolderClick,
                    size = 40.dp
                ) {
                    Icon(
                        imageVector = Icons.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}


@Composable
private fun AddNewFolderDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = stringResource(R.string.dialog_add_new_folder_title))
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {
                    Text(text = stringResource(R.string.dialog_add_new_folder_textfield_hint))
                },
                shape = MaterialTheme.shapes.medium
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(text)
                }
            ) {
                Text(text = stringResource(R.string.dialog_add_new_folder_button_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = stringResource(R.string.dialog_add_new_folder_button_dismiss))
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeFolderBottomSheet(
    items: List<Folder>,
    isFolderChecked: (Folder) -> Boolean,
    onFolderCheckedChange: (Folder, Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        LazyColumn(
            contentPadding = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
                .asPaddingValues()
        ) {
            items(
                items = items,
                key = { it.id }
            ) { folder ->
                ChangeFolderItem(
                    folder = folder,
                    checked = isFolderChecked(folder),
                    onCheckedChange = { checked ->
                        onFolderCheckedChange(folder, checked)
                    }
                )
            }
        }
    }
}

@Composable
private fun ChangeFolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Surface(
        onClick = {
            onCheckedChange(!checked)
        },
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        interactionSource = interactionSource
    ) {
        ListItem(
            modifier = modifier.height(48.dp),
            leadingContent = {
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        onCheckedChange(!checked)
                    },
                    interactionSource = interactionSource
                )
            },
            headlineContent = {
                Text(
                    text = folder.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
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
    LazyVerticalStaggeredGridWithSelection(
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
                when (selectionState.inSelectionMode) {
                    true -> selectionState.updateItem(note)
                    else -> onCLickNoteCard(note)
                }
            },
            onLongClick = {
                selectionState.updateItem(note)
            },
            onFavoriteClick = {
                when (selectionState.inSelectionMode) {
                    true -> selectionState.updateItem(note)
                    else -> onFavoriteClick(note)
                }
            }
        )
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    selected: Boolean = false,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    val sharedTransitionScope = LocalNavSharedElementScope.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    with(sharedTransitionScope) {
        NoteCard(
            modifier = modifier
                .sharedBoundsRevealWithShapeMorph(
                    sharedContentState = rememberSharedContentState(NoteSharedElementKey(note.id)),
                    animatedVisibilityScope = animatedVisibilityScope,
                    targetShapeCornerRadius = 0.dp,
                    restingShapeCornerRadius = NoteCardDefaults.ShapeCornerRadius,
                    keepChildrenSizePlacement = false,
                ),
            selected = selected,
            colors = NoteCardDefaults.noteCardColors(
                containerColor = MaterialTheme.noteColors[note.colorId].harmonized
            ),
            headlineContent = {
                Text(
                    modifier = Modifier
                        .skipToLookaheadSize(),
                    text = note.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            trailingContent = {
                ShapedIconToggleButton(
                    modifier = Modifier
                        .skipToLookaheadSize(),
                    checked = selected,
                    onCheckedChange = { onFavoriteClick() }
                ) {
                    Icon(
                        imageVector = if (!selected) {
                            if (!note.isFavorite) {
                                Icons.FavoriteBorder
                            } else {
                                Icons.Favorite
                            }
                        } else {
                            Icons.Done
                        },
                        contentDescription = ""
                    )
                }
            },
            supportingContent = {
                Text(
                    modifier = Modifier
                        .skipToLookaheadSize(),
                    text = note.timeStamp.convertToDateTime(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            bodyContent = {
                Text(
                    modifier = Modifier
                        .skipToLookaheadSize(),
                    text = note.content,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            onClick = { onClick() },
            onLongClick = { onLongClick() }
        )
    }
}

@Composable
private fun EmptyScreenPlaceHolder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.empty_screen_placeholder_text),
            textAlign = TextAlign.Center
        )
    }
}
