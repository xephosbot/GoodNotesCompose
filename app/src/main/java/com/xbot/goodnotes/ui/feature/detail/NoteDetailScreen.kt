package com.xbot.goodnotes.ui.feature.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.goodnotes.ui.component.Scaffold
import com.xbot.ui.theme.NoteColors
import com.xbot.ui.theme.harmonized

@Composable
fun NoteDetailScreen(
    viewModel: NoteDetailViewModel = hiltViewModel(),
    navigate: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NoteDetailScreenContent(
        state = state,
        onEvent = viewModel::onAction,
        navigate = navigate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreenContent(
    modifier: Modifier = Modifier,
    state: NoteDetailScreenState,
    onEvent: (NoteDetailScreenEvent) -> Unit,
    navigate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.noteTitle) },
                navigationIcon = {
                    IconButton(onClick = navigate) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = LocalContentColor.current,
                    titleContentColor = LocalContentColor.current
                )
            )
        },
        containerColor = NoteColors[state.noteColorId].harmonized,
        contentColor = Color.Black
    ) {
        Column {
            Text(text = state.noteText)
        }
    }
}