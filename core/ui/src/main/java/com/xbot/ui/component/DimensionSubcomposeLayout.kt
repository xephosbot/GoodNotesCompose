package com.xbot.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.SubcomposeMeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMapIndexed
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * SubcomposeLayout that [SubcomposeMeasureScope.subcompose] [contents]
 * and gets total size of [contents] and passes this size to [dependentContent].
 * This layout passes exact size of content unlike
 * BoxWithConstraints which returns [Constraints] that doesn't match Composable dimensions under
 * some circumstances
 *
 * @param contents Composable is used for calculating size and pass it
 * to Composables that depend on it
 *
 * @param dependentContent Composable requires dimensions of [contents] to set its size.
 * One example for this is overlay over Composable that should match [dependentContent] size.
 *
 */
@Composable
internal fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    contents: ImmutableList<@Composable @UiComposable () -> Unit>,
    dependentContent: @Composable (ImmutableList<Size>) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints: Constraints ->

        // Subcompose(compose only a section) main content and get Placeable
        val mainPlaceables = contents.fastMapIndexed { index, content ->
            subcompose(Slots.Main(index), content).fastMap {
                it.measure(constraints.copy(minWidth = 0, minHeight = 0))
            }.first()
        }

        val dependentContentsSize = if (contents.isEmpty()) {
            persistentListOf()
        } else {
            mainPlaceables.fastMap { Size(it.width.toFloat(), it.height.toFloat()) }.toImmutableList()
        }

        val dependentPlaceable: Placeable = subcompose(Slots.Dependent) {
            dependentContent(dependentContentsSize)
        }.fastMap { measurable: Measurable ->
            measurable.measure(constraints)
        }.first()

        layout(dependentPlaceable.width, dependentPlaceable.height) {
            dependentPlaceable.placeRelative(0, 0)
        }
    }
}

/**
 * SubcomposeLayout that [SubcomposeMeasureScope.subcompose] [content]
 * and gets total size of [content] and passes this size to [dependentContent].
 * This layout passes exact size of content unlike
 * BoxWithConstraints which returns [Constraints] that doesn't match Composable dimensions under
 * some circumstances
 *
 * @param content Composable is used for calculating size and pass it
 * to Composables that depend on it
 *
 * @param dependentContent Composable requires dimensions of [content] to set its size.
 * One example for this is overlay over Composable that should match [dependentContent] size.
 *
 */
@Composable
internal fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    dependentContent: @Composable (Size) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints: Constraints ->

        // Subcompose(compose only a section) main content and get Placeable
        val mainPlaceable = subcompose(Slots.Main(0), content).fastMap {
            it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }.maxByOrNull { it.height }

        val dependentContentSize = if (mainPlaceable == null) {
            Size(0f, 0f)
        } else {
            Size(mainPlaceable.width.toFloat(), mainPlaceable.height.toFloat())
        }

        val dependentPlaceable: Placeable = subcompose(Slots.Dependent) {
            dependentContent(dependentContentSize)
        }.fastMap { measurable: Measurable ->
            measurable.measure(constraints)
        }.first()

        layout(dependentPlaceable.width, dependentPlaceable.height) {
            dependentPlaceable.placeRelative(0, 0)
        }
    }
}

internal sealed interface Slots {
    data class Main(val id: Int): Slots
    data object Dependent: Slots
}
