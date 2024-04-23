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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.goodnotes.ui.changeStatusBarAppearance
import com.xbot.goodnotes.ui.plus
import com.xbot.ui.component.Scaffold
import com.xbot.ui.component.ShapedIconButtonDefaults
import com.xbot.ui.component.ShapedIconToggleButton
import com.xbot.ui.icon.Icons
import com.xbot.ui.theme.NoteColors
import com.xbot.ui.theme.harmonized

@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    changeStatusBarAppearance(isLightAppearance = false)

    NoteDetailScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteDetailScreenContent(
    modifier: Modifier = Modifier,
    state: NoteDetailScreenState,
    onAction: (NoteDetailScreenAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    val titleTextFieldState = rememberSaveable(state.noteTitle, saver = TextFieldState.Saver) {
        TextFieldState(state.noteTitle)
    }
    val contentTextFieldState = rememberSaveable(state.noteText, saver = TextFieldState.Saver) {
        TextFieldState(state.noteText)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NoteDetailScreenAppBar(
                isFavorite = state.noteIsFavorite,
                containerColor = NoteColors[state.noteColorId].harmonized,
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
                containerColor = NoteColors[state.noteColorId].harmonized,
                canUndo = contentTextFieldState.undoState.canUndo,
                canRedo = contentTextFieldState.undoState.canRedo,
                onPaletteClick = {},
                onUndoClick = {
                    contentTextFieldState.undoState.undo()
                },
                onRedoClick = {
                    contentTextFieldState.undoState.redo()
                }
            )
        },
        containerColor = NoteColors[state.noteColorId].harmonized,
        contentColor = Color.Black
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding + PaddingValues(horizontal = 16.dp))
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                state = titleTextFieldState,
                hint = "Title",
                textStyle = MaterialTheme.typography.displayMedium
            )
            TextField(
                state = contentTextFieldState,
                hint = "Content",
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
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

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    hint: String,
    textStyle: TextStyle
) {
    BasicTextField(
        modifier = modifier,
        state = state,
        textStyle = textStyle,
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

@Composable
private fun ColorsBottomAppBar(
    modifier: Modifier = Modifier,
    colorsList: List<Color>,
    containerColor: Color,
    contentColor: Color = Color.Black,
    isColorSelected: (Int) -> Boolean,
    onClickColor: (Int) -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
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
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
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
