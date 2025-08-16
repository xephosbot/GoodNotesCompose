package com.xbot.goodnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.xbot.goodnotes.ui.feature.detail.NoteDetailScreen
import com.xbot.goodnotes.ui.feature.detail.NoteDetailViewModel
import com.xbot.goodnotes.ui.feature.note.NoteScreen
import com.xbot.goodnotes.ui.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun GoodNotesNavHost(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    startDestination: NavKey = Notes
) {
    val backStack = rememberNavBackStack(startDestination)
    val bottomSheetSceneStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

    SharedTransitionLayout {
        NavDisplay(
            modifier = modifier
                .fillMaxSize()
                .background(containerColor),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberSharedElementNavEntryDecorator(this),
                rememberViewModelStoreNavEntryDecorator()
            ),
            sceneStrategy = bottomSheetSceneStrategy,
            entryProvider = entryProvider {
                entry<Notes> {
                    NoteScreen(
                        navigateToDetails = { noteId ->
                            backStack.add(NoteDetail(noteId))
                        },
                        navigateToSettings = {
                            backStack.add(Settings)
                        }
                    )
                }
                entry<NoteDetail> { key ->
                    val viewModel = hiltViewModel<NoteDetailViewModel, NoteDetailViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                    NoteDetailScreen(
                        viewModel = viewModel,
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
                entry<Settings>(
                    metadata = BottomSheetSceneStrategy.bottomSheet(
                        ModalBottomSheetProperties(true)
                    )
                ) {
                    SettingsScreen()
                }
            }
        )
    }
}

@Serializable
object Notes : NavKey

@Serializable
data class NoteDetail(val noteId: Long) : NavKey

@Serializable
object Settings : NavKey
