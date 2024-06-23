package com.xbot.goodnotes.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        modifier = modifier,
        sheetShape = RectangleShape,
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetContentColor = contentColor,
        scrimColor = scrimColor,
        content = content
    )
}

@ExperimentalMaterial3Api
fun NavGraphBuilder.bottomSheet(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable ColumnScope.(backstackEntry: NavBackStackEntry) -> Unit
) {
    addDestination(
        BottomSheetNavigator.Destination(
            navigator = provider[BottomSheetNavigator::class],
            content = { BottomSheet { content(it) } }
        ).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    modifier: Modifier = Modifier,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = BottomSheetDefaults.Elevation,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    windowInsets: WindowInsets = BottomSheetDefaults.windowInsets,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .widthIn(max = sheetMaxWidth)
            .fillMaxWidth(),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets.only(WindowInsetsSides.Bottom))
        ) {
            if (dragHandle != null) {
                Box(Modifier.align(Alignment.CenterHorizontally)) {
                    dragHandle()
                }
            }
            content()
        }
    }
}
