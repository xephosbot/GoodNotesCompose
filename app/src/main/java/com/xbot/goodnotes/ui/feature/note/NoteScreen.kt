package com.xbot.goodnotes.ui.feature.note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xbot.goodnotes.R
import com.xbot.goodnotes.model.Note
import com.xbot.goodnotes.ui.component.LargeTopAppBarWithSelectionMode
import com.xbot.goodnotes.ui.component.LazyVerticalStaggeredGrid
import com.xbot.goodnotes.ui.component.Scaffold
import com.xbot.goodnotes.ui.plus
import com.xbot.ui.component.AnimatedFloatingActionButton
import com.xbot.ui.component.NoteCard
import com.xbot.ui.component.NoteCardDefaults
import com.xbot.ui.component.SelectableChip
import com.xbot.ui.component.SelectableChipBadge
import com.xbot.ui.component.ShapedIconButton
import com.xbot.ui.component.ShapedIconButtonDefaults
import com.xbot.ui.component.isScrollingUp
import com.xbot.ui.theme.NoteColors
import com.xbot.ui.theme.harmonized
import kotlinx.collections.immutable.toImmutableList

@Composable
fun NoteScreen(
    viewModel: NoteViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun NoteScreenContent(
    modifier: Modifier = Modifier,
    state: NoteScreenState,
    onEvent: (NoteScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val selectedNotes = remember { mutableStateListOf<Note>() }
    val inSelectionMode by remember(selectedNotes) {
        derivedStateOf { selectedNotes.isNotEmpty() }
    }
    val lazyGridState = rememberLazyStaggeredGridState()

    BackHandler(enabled = inSelectionMode) {
        selectedNotes.clear()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBarWithSelectionMode(
                selected = inSelectionMode,
                title = { selected ->
                    if (!selected) stringResource(R.string.my_notes_title) else "Notes:\n${selectedNotes.count()}"
                },
                navigationIcon = { selected ->
                    if (selected) {
                        IconButton(onClick = { selectedNotes.clear() }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = ""
                            )
                        }
                    }
                },
                actions = { selected ->
                    if (!selected) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = ""
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            onEvent(NoteScreenEvent.DeleteNotes(selectedNotes.toImmutableList()))
                            selectedNotes.clear()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = ""
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = ""
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.foldersList) { folder ->
                        SelectableChip(
                            selected = state.currentFolderId == folder.id,
                            onClick = {
                                onEvent(NoteScreenEvent.OpenFolder(folder.id))
                            },
                            label = { Text(text = folder.name) },
                            leadingIcon = {
                                SelectableChipBadge(text = folder.noteCount.toString())
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        floatingActionButton = {
            AnimatedFloatingActionButton(
                onClick = { /*TODO*/ },
                visible = lazyGridState.isScrollingUp().value && !inSelectionMode
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier,
            columns = StaggeredGridCells.Adaptive(180.dp),
            state = lazyGridState,
            items = state.notesList,
            key = { it.id },
            refreshKey = state.currentFolderId,
            contentPadding = innerPadding + PaddingValues(horizontal = 4.dp),
            verticalItemSpacing = 4.dp,
            horizontalItemSpacing = 4.dp
        ) { note ->
            val selected = selectedNotes.contains(note)

            NoteCard(
                note = note,
                selected = selected,
                onClick = { isSelected ->
                    if (inSelectionMode) {
                        if (!isSelected) {
                            selectedNotes.add(note)
                        } else {
                            selectedNotes.remove(note)
                        }
                    } else {
                        //onEvent(NotesEvent.Shuffle)
                    }
                },
                onLongClick = { isSelected ->
                    if (!isSelected) {
                        selectedNotes.add(note)
                    } else {
                        selectedNotes.remove(note)
                    }
                },
                onFavoriteClick = { isSelected ->
                    //TODO:
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    selected: Boolean = false,
    onClick: (Boolean) -> Unit,
    onLongClick: (Boolean) -> Unit,
    onFavoriteClick: (Boolean) -> Unit
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
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            ShapedIconButton(
                colors = ShapedIconButtonDefaults.shapedIconButtonColors(
                    containerColor = if (!selected) LocalContentColor.current.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primary,
                    contentColor = if (!selected) LocalContentColor.current else MaterialTheme.colorScheme.onPrimary
                ),
                onClick = { onFavoriteClick(selected) }
            ) {
                Icon(
                    imageVector = if (!selected) Icons.Filled.Favorite else Icons.Filled.Done,
                    contentDescription = ""
                )
            }
        },
        supportingContent = { Text(text = note.dateTime) },
        bodyContent = { Text(text = note.content) },
        onClick = { onClick(selected) },
        onLongClick = { onLongClick(selected) }
    )
}
