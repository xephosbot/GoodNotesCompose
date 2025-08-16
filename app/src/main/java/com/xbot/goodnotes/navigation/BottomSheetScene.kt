package com.xbot.goodnotes.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.OverlayScene
import androidx.navigation3.ui.Scene
import androidx.navigation3.ui.SceneStrategy

@OptIn(ExperimentalMaterial3Api::class)
internal class BottomSheetScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val entry: NavEntry<T>,
    private val bottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: (count: Int) -> Unit,
) : OverlayScene<T> {

    override val entries: List<NavEntry<T>> = listOf(entry)

    override val content: @Composable (() -> Unit) = {
        ModalBottomSheet(
            onDismissRequest = { onBack(1) },
            properties = bottomSheetProperties
        ) {
            entry.Content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
    @Composable
    override fun calculateScene(
        entries: List<NavEntry<T>>,
        onBack: (Int) -> Unit
    ): Scene<T>? {
        val lastEntry = entries.lastOrNull()
        val bottomSheetProperties = lastEntry?.metadata?.get(BOTTOM_SHEET_KEY) as? ModalBottomSheetProperties
        return bottomSheetProperties?.let { properties ->
            BottomSheetScene(
                key = lastEntry.contentKey,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                entry = lastEntry,
                bottomSheetProperties = properties,
                onBack = onBack,
            )
        }
    }

    companion object {
        fun bottomSheet(
            bottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties
        ): Map<String, Any> = mapOf(BOTTOM_SHEET_KEY to bottomSheetProperties)

        internal const val BOTTOM_SHEET_KEY = "bottomSheet"
    }
}