package com.xbot.goodnotes.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.xbot.domain.model.AppTheme
import com.xbot.goodnotes.MainActivityUiState

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection) + other.calculateStartPadding(
            layoutDirection
        ),
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        end = this.calculateEndPadding(layoutDirection) + other.calculateEndPadding(layoutDirection),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
    )
}

@SuppressLint("ComposableNaming")
@Composable
fun changeStatusBarAppearance(isLightAppearance: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(view) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            val originalIsAppearanceLightStatusBars = insetsController.isAppearanceLightStatusBars
            val originalIsAppearanceLightNavigationBars = insetsController.isAppearanceLightNavigationBars

            insetsController.isAppearanceLightStatusBars = !isLightAppearance
            insetsController.isAppearanceLightNavigationBars = !isLightAppearance

            onDispose {
                insetsController.isAppearanceLightStatusBars = originalIsAppearanceLightStatusBars
                insetsController.isAppearanceLightNavigationBars = originalIsAppearanceLightNavigationBars
            }
        }
    }
}

@Composable
fun shouldUseDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> uiState.useDynamicTheme
}

@Composable
fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.appTheme) {
        AppTheme.System -> isSystemInDarkTheme()
        AppTheme.Light -> false
        AppTheme.Dark -> true
    }
}

fun ComponentActivity.enableEdgeToEdge(
    detectDarkMode: (Resources) -> Boolean = { resources ->
        (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
    }
) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT,
            detectDarkMode
        ),
        navigationBarStyle = SystemBarStyle.auto(
            android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
            android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b),
            detectDarkMode
        )
    )
}
