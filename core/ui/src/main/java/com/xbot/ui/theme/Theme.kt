package com.xbot.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue90,
    onPrimary = DarkBlue50,
    primaryContainer = DarkBlue55,
    onPrimaryContainer = LightBlue100,
    secondary = LightBlue50,
    onSecondary = DarkBlue25,
    secondaryContainer = DarkBlue35,
    onSecondaryContainer = LightBlue80,
    tertiary = LightMagenta60,
    onTertiary = DarkMagenta20,
    tertiaryContainer = DarkMagenta25,
    onTertiaryContainer = LightMagenta100,
    error = LightRed90,
    errorContainer = DarkRed65,
    onError = DarkRed60,
    onErrorContainer = LightRed100,
    background = DarkBlue10,
    onBackground = LightPurple45,
    surface = DarkBlue10,
    onSurface = LightPurple45,
    surfaceVariant = DarkBlue20,
    onSurfaceVariant = LightBlue40,
    outline = Blue35,
    inverseSurface = LightPurple45,
    inverseOnSurface = DarkBlue10,
    inversePrimary = Blue70,
    surfaceTint = LightBlue90,
    outlineVariant = DarkBlue20,
    scrim = Black
)

private val LightColorScheme = lightColorScheme(
    primary = Blue70,
    onPrimary = White,
    primaryContainer = LightBlue100,
    onPrimaryContainer = DarkBlue60,
    secondary = Blue30,
    onSecondary = White,
    secondaryContainer = LightBlue80,
    onSecondaryContainer = DarkBlue30,
    tertiary = Magenta30,
    onTertiary = White,
    tertiaryContainer = LightMagenta100,
    onTertiaryContainer = DarkMagenta30,
    error = Red60,
    errorContainer = LightRed100,
    onError = White,
    onErrorContainer = DarkRed50,
    background = LightPurple100,
    onBackground = DarkBlue10,
    surface = LightPurple100,
    onSurface = DarkBlue10,
    surfaceVariant = LightBlue60,
    onSurfaceVariant = DarkBlue20,
    outline = Blue25,
    inverseSurface = DarkPurple10,
    inverseOnSurface = LightPurple50,
    inversePrimary = LightBlue90,
    surfaceTint = Blue70,
    outlineVariant = LightBlue40,
    scrim = Black
)

@Composable
fun GoodNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && supportsDynamicTheming() -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val noteColors = when {
        darkTheme -> NoteColors(DarkNoteColors)
        else -> NoteColors(LightNoteColors)
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalNoteColors provides noteColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = Shapes,
            typography = Typography,
            content = content
        )
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportsDynamicTheming() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
