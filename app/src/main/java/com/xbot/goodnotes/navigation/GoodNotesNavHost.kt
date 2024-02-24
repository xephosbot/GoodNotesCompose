package com.xbot.goodnotes.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xbot.goodnotes.ui.GoodNotesAppState
import com.xbot.goodnotes.ui.feature.detail.NoteDetailScreen
import com.xbot.goodnotes.ui.feature.note.NoteScreen

@Composable
fun GoodNotesNavHost(
    modifier: Modifier = Modifier,
    appState: GoodNotesAppState,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: String = "notesScreen"
) {
    NavHost(
        modifier = modifier.background(containerColor),
        navController = appState.navController,
        startDestination = startDestination
    ) {
        composable(
            route = "notesScreen"
        ) {
            NoteScreen { noteId ->
                appState.navController.navigate("noteDetailScreen/${noteId}") {
                    restoreState = true
                }
            }
        }
        composable(
            route = "noteDetailScreen/{$NOTE_ID_ARG}",
            arguments = listOf(
                navArgument(name = NOTE_ID_ARG) { type = NavType.LongType }
            )
        ) {
            NoteDetailScreen {
                appState.navController.popBackStack()
            }
        }
    }
}

private const val NOTE_ID_ARG = "noteId"
