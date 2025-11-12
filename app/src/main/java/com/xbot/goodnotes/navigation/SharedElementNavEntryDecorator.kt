package com.xbot.goodnotes.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntryDecorator

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun rememberSharedElementNavEntryDecorator(
    scope: SharedTransitionScope
): NavEntryDecorator<Any> = remember { SharedElementNavEntryDecorator(scope) }

@OptIn(ExperimentalSharedTransitionApi::class)
private class SharedElementNavEntryDecorator(
    scope: SharedTransitionScope
): NavEntryDecorator<Any>(
    decorate = { entry ->
        CompositionLocalProvider(LocalNavSharedElementScope provides scope) { entry.Content() }
    }
)

@ExperimentalSharedTransitionApi
val LocalNavSharedElementScope = compositionLocalOf<SharedTransitionScope> {
    throw IllegalArgumentException("No Scope found")
}

data class NoteSharedElementKey(val noteId: Long)
