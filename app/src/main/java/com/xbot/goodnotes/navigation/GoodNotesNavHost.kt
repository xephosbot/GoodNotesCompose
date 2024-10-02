package com.xbot.goodnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.navigation.rememberBottomSheetNavigator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.xbot.goodnotes.ui.feature.detail.NoteDetailScreen
import com.xbot.goodnotes.ui.feature.note.NoteScreen
import com.xbot.goodnotes.ui.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GoodNotesNavHost(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: Any = Notes
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    SharedTransitionLayout {
        ModalBottomSheetLayout(bottomSheetNavigator) {
            NavHost(
                modifier = modifier
                    .fillMaxSize()
                    .background(containerColor),
                navController = navController,
                startDestination = startDestination
            ) {
                sharedElementComposable<Notes>(this@SharedTransitionLayout) {
                    NoteScreen(
                        navigateToDetails = { noteId ->
                            navController.navigate(NoteDetail(noteId)) {
                                restoreState = true
                            }
                        },
                        navigateToSettings = {
                            navController.navigate(Settings)
                        }
                    )
                }
                sharedElementComposable<NoteDetail>(this@SharedTransitionLayout) {
                    NoteDetailScreen(
                        onNavigateBack = {
                            navController.navigateUp()
                        }
                    )
                }
                bottomSheet<Settings> {
                    SettingsScreen()
                }
            }
        }
    }
}

@Serializable
object Notes

@Serializable
data class NoteDetail(val noteId: Long)

@Serializable
object Settings
