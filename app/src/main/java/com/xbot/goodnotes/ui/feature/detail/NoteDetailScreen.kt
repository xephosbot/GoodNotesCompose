package com.xbot.goodnotes.ui.feature.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.goodnotes.R
import com.xbot.goodnotes.ui.plus
import com.xbot.ui.component.Scaffold
import com.xbot.ui.component.ShapedIconButtonDefaults
import com.xbot.ui.component.ShapedIconToggleButton
import com.xbot.ui.icon.Icons
import com.xbot.ui.theme.harmonized
import com.xbot.ui.theme.noteColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    NoteDetailScreenContent(
        state = state,
        titleTextFieldState = viewModel.titleTextFieldState,
        contentTextFieldState = viewModel.contentTextFieldState,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDetailScreenContent(
    modifier: Modifier = Modifier,
    state: NoteDetailScreenState,
    titleTextFieldState: TextFieldState,
    contentTextFieldState: TextFieldState,
    onAction: (NoteDetailScreenAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    var showColorPickerBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            NoteDetailScreenAppBar(
                isFavorite = state.noteIsFavorite,
                containerColor = Color.Transparent,
                onArrowBackClick = {
                    onAction(NoteDetailScreenAction.Save)
                    onNavigateBack()
                },
                onFavoriteClick = {
                    onAction(NoteDetailScreenAction.UpdateNote(!state.noteIsFavorite))
                }
            )
        },
        bottomBar = {
            NoteDetailScreenBottomAppBar(
                containerColor = MaterialTheme.noteColors[state.noteColorId].harmonized,
                canUndo = contentTextFieldState.undoState.canUndo,
                canRedo = contentTextFieldState.undoState.canRedo,
                onPaletteClick = {
                    showColorPickerBottomSheet = true
                },
                onUndoClick = {
                    contentTextFieldState.undoState.undo()
                },
                onRedoClick = {
                    contentTextFieldState.undoState.redo()
                }
            )
        },
        containerColor = MaterialTheme.noteColors[state.noteColorId].harmonized,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding + PaddingValues(horizontal = 16.dp))
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                state = titleTextFieldState,
                hint = stringResource(R.string.text_field_hint_title),
                textStyle = MaterialTheme.typography.displayMedium
            )
            TextField(
                state = contentTextFieldState,
                hint = stringResource(R.string.text_field_hint_content),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }

    if (showColorPickerBottomSheet) {
        ColorPickerBottomSheet(
            colorsList = MaterialTheme.noteColors.value,
            containerColor = MaterialTheme.noteColors[state.noteColorId].harmonized,
            isColorSelected = { colorId ->
                state.noteColorId == colorId
            },
            onClickColor = { colorId ->
                onAction(NoteDetailScreenAction.ChangeNoteColor(colorId))
            },
            onDismissRequest = {
                showColorPickerBottomSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteDetailScreenAppBar(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    containerColor: Color,
    onArrowBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { },
        navigationIcon = {
            IconButton(onClick = onArrowBackClick) {
                Icon(
                    imageVector = Icons.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = when (isFavorite) {
                        true -> Icons.Favorite
                        else -> Icons.FavoriteBorder
                    },
                    contentDescription = ""
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        )
    )
}

@Composable
private fun NoteDetailScreenBottomAppBar(
    modifier: Modifier = Modifier,
    containerColor: Color,
    canUndo: Boolean,
    canRedo: Boolean,
    onPaletteClick: () -> Unit,
    onUndoClick: () -> Unit,
    onRedoClick: () -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = containerColor,
        windowInsets = BottomAppBarDefaults.windowInsets.union(WindowInsets.ime)
    ) {
        IconButton(onClick = onPaletteClick) {
            Icon(
                imageVector = Icons.Palette,
                contentDescription = ""
            )
        }
        IconButton(
            onClick = onUndoClick,
            enabled = canUndo
        ) {
            Icon(
                imageVector = Icons.Undo,
                contentDescription = ""
            )
        }
        IconButton(
            onClick = onRedoClick,
            enabled = canRedo
        ) {
            Icon(
                imageVector = Icons.Redo,
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    hint: String,
    textStyle: TextStyle
) {
    BasicTextField2(
        modifier = modifier,
        state = state,
        textStyle = textStyle.copy(color = LocalContentColor.current),
        decorator = { innerTextField ->
            if (state.text.isEmpty()) {
                Text(
                    text = hint,
                    color = LocalContentColor.current.copy(alpha = 0.38f),
                    style = textStyle
                )
            }
            innerTextField()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorPickerBottomSheet(
    modifier: Modifier = Modifier,
    colorsList: List<Color>,
    containerColor: Color,
    contentColor: Color = LocalContentColor.current,
    isColorSelected: (Int) -> Boolean,
    onClickColor: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        windowInsets = WindowInsets(0),
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        BottomAppBar(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = contentColor
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(
                    items = colorsList,
                    key = { _, color -> color.toArgb() }
                ) { index, color ->
                    ColorItem(
                        color = color,
                        selected = isColorSelected(index),
                        onClick = { onClickColor(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    ShapedIconToggleButton(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                ),
                shape = MaterialTheme.shapes.extraSmall
            ),
        checked = selected,
        onCheckedChange = { onClick() },
        colors = ShapedIconButtonDefaults.shapedIconToggleButtonColors(color)
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Done,
                contentDescription = ""
            )
        }
    }
}
