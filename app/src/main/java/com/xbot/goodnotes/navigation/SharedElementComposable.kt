package com.xbot.goodnotes.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import com.xbot.ui.animation.materialSharedAxisZIn
import com.xbot.ui.animation.materialSharedAxisZOut
import kotlin.reflect.KType

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.sharedElementComposable(
    sharedTransitionScope: SharedTransitionScope,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { materialSharedAxisZIn(forward = true) },
    exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { materialSharedAxisZOut(forward = true) },
    popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { materialSharedAxisZIn(forward = false) },
    popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { materialSharedAxisZOut(forward = false) },
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(route, arguments, deepLinks, enterTransition, exitTransition, popEnterTransition, popExitTransition) {
        CompositionLocalProvider(
            LocalSharedElementScopes provides SharedElementScopes(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this@composable
            )
        ) {
            content(it)
        }
    }
}

@ExperimentalSharedTransitionApi
inline fun <reified T : Any> NavGraphBuilder.sharedElementComposable(
    sharedTransitionScope: SharedTransitionScope,
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline enterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { materialSharedAxisZIn(forward = true) },
    noinline exitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { materialSharedAxisZOut(forward = true) },
    noinline popEnterTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = { materialSharedAxisZIn(forward = false) },
    noinline popExitTransition: (@JvmSuppressWildcards AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = { materialSharedAxisZOut(forward = false) },
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(typeMap, deepLinks, enterTransition, exitTransition, popEnterTransition, popExitTransition) {
        CompositionLocalProvider(
            LocalSharedElementScopes provides SharedElementScopes(
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = this@composable
            )
        ) {
            content(it)
        }
    }
}

@ExperimentalSharedTransitionApi
val LocalSharedElementScopes = compositionLocalOf { SharedElementScopes() }

@ExperimentalSharedTransitionApi
data class SharedElementScopes (
    val sharedTransitionScope: SharedTransitionScope? = null,
    val animatedVisibilityScope: AnimatedVisibilityScope? = null
)

data class NoteSharedElementKey(
    val noteId: Long,
    val type: NoteSharedElementType
)

enum class NoteSharedElementType {
    Bounds,
    Content
}
