package com.xbot.goodnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xbot.goodnotes.ui.feature.detail.NoteDetailScreen
import com.xbot.goodnotes.ui.feature.note.NoteScreen
import com.xbot.goodnotes.ui.feature.settings.SettingsScreen

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun GoodNotesNavHost(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: String = "notesScreen"
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    SharedTransitionLayout(
        modifier = modifier.background(containerColor),
    ) {
        ModalBottomSheetLayout(
            bottomSheetNavigator = bottomSheetNavigator
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {
                sharedElementComposable(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    route = "notesScreen"
                ) {
                    NoteScreen(
                        navigateToDetails = { noteId ->
                            navController.navigate("noteDetailScreen/${noteId}") {
                                restoreState = true
                            }
                        },
                        navigateToSettings = {
                            navController.navigate("settingsScreen")
                        }
                    )
                }
                sharedElementComposable(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    route = "noteDetailScreen/{$NOTE_ID_ARG}",
                    arguments = listOf(
                        navArgument(name = NOTE_ID_ARG) { type = NavType.LongType }
                    )
                ) {
                    NoteDetailScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                bottomSheet(
                    route = "settingsScreen"
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

private const val NOTE_ID_ARG = "noteId"
