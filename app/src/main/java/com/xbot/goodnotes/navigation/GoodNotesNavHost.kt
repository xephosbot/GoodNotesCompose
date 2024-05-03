package com.xbot.goodnotes.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.xbot.goodnotes.ui.feature.detail.NoteDetailScreen
import com.xbot.goodnotes.ui.feature.note.NoteScreen
import com.xbot.goodnotes.ui.feature.settings.SettingsScreen
import com.xbot.ui.animation.materialSharedAxisZIn
import com.xbot.ui.animation.materialSharedAxisZOut

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun GoodNotesNavHost(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: String = "notesScreen"
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator
    ) {
        NavHost(
            modifier = modifier.background(containerColor),
            navController = navController,
            startDestination = startDestination,
            enterTransition = {
                materialSharedAxisZIn(forward = true)
            },
            exitTransition = {
                materialSharedAxisZOut(forward = true)
            },
            popEnterTransition = {
                materialSharedAxisZIn(forward = false)
            },
            popExitTransition = {
                materialSharedAxisZOut(forward = false)
            }
        ) {
            composable(
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
            composable(
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

private const val NOTE_ID_ARG = "noteId"
