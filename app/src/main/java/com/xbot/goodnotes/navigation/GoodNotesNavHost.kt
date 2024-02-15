package com.xbot.goodnotes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    startDestination: String = "notesScreen"
) {
    NavHost(
        modifier = modifier,
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
            route = "noteDetailScreen/{noteId}",
            arguments = listOf(
                navArgument(name = "noteId") { type = NavType.LongType }
            )
        ) {
            NoteDetailScreen {
                appState.navController.popBackStack()
            }
        }
    }
}