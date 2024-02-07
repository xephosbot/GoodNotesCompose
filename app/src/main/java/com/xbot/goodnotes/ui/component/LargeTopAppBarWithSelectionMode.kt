package com.xbot.goodnotes.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.xbot.ui.component.LargeTopAppBarTitleScale

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun LargeTopAppBarWithSelectionMode(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: @Composable (Boolean) -> String,
    navigationIcon: @Composable (Boolean) -> Unit,
    actions: @Composable RowScope.(Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessLow),
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Column {
        LargeTopAppBarWithSelectionModeContent(
            modifier = modifier,
            selected = selected,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            scrollBehavior = scrollBehavior,
            animationSpec = animationSpec
        )
        content()
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
private fun LargeTopAppBarWithSelectionModeContent(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: @Composable (Boolean) -> String,
    navigationIcon: @Composable (Boolean) -> Unit,
    actions: @Composable RowScope.(Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    animationSpec: FiniteAnimationSpec<Float> = spring(stiffness = Spring.StiffnessLow)
) {
    var currentTitle by remember { mutableStateOf("") }

    Box {
        LargeTopAppBarTitleScale(
            modifier = modifier,
            title = {
                Text(text = title(false))
            },
            navigationIcon = {
                navigationIcon(false)
            },
            actions = {
                actions(false)
            },
            scrollBehavior = scrollBehavior
        )
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(animationSpec),
            exit = fadeOut(animationSpec)
        ) {
            if (!transition.isRunning && selected) {
                currentTitle = title(true)
            }

            LargeTopAppBarTitleScale(
                modifier = modifier,
                title = {
                    Text(text = currentTitle)
                },
                navigationIcon = {
                    navigationIcon(true)
                },
                actions = {
                    actions(true)
                },
                scrollBehavior = scrollBehavior
            )
        }
    }
}