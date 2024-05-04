/**
 * Just workaround for https://issuetracker.google.com/issues/328642898
 */

@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package com.xbot.ui.theme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.TonalPalette
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicTonalPalette
import androidx.compose.material3.lightColorScheme

/**
 * Creates a light dynamic color scheme.
 *
 * Use this function to create a color scheme based off the system wallpaper. If the developer
 * changes the wallpaper this color scheme will change accordingly. This dynamic scheme is a
 * light theme variant.
 *
 * @param context The context required to get system resource data.
 */
@RequiresApi(Build.VERSION_CODES.S)
fun dynamicLightColorScheme(context: Context): ColorScheme {
    val tonalPalette = dynamicTonalPalette(context)
    return if (Build.VERSION.SDK_INT >= 34) {
        // SDKs 34 and greater return appropriate Chroma6 values for neutral palette
        dynamicLightColorScheme34(tonalPalette)
    } else {
        // SDKs 31-33 return Chroma4 values for neutral palette, we instead leverage neutral
        // variant which provides chroma8 for less grey tones.
        dynamicLightColorScheme31(tonalPalette)
    }
}

/**
 * Creates a dark dynamic color scheme.
 *
 * Use this function to create a color scheme based off the system wallpaper. If the developer
 * changes the wallpaper this color scheme will change accordingly. This dynamic scheme is a dark
 * theme variant.
 *
 * @param context The context required to get system resource data.
 */
@RequiresApi(Build.VERSION_CODES.S)
fun dynamicDarkColorScheme(context: Context): ColorScheme {
    val tonalPalette = dynamicTonalPalette(context)
    return if (Build.VERSION.SDK_INT >= 34) {
        // SDKs 34 and greater return appropriate Chroma6 values for neutral palette
        dynamicDarkColorScheme34(tonalPalette)
    } else {
        // SDKs 31-33 return Chroma4 values for neutral palette, we instead leverage neutral
        // variant which provides chroma8 for less grey tones.
        dynamicDarkColorScheme31(tonalPalette)
    }
}

@RequiresApi(31)
internal fun dynamicLightColorScheme31(tonalPalette: TonalPalette) = lightColorScheme(
    primary = tonalPalette.primary40,
    onPrimary = tonalPalette.primary100,
    primaryContainer = tonalPalette.primary90,
    onPrimaryContainer = tonalPalette.primary10,
    inversePrimary = tonalPalette.primary80,
    secondary = tonalPalette.secondary40,
    onSecondary = tonalPalette.secondary100,
    secondaryContainer = tonalPalette.secondary90,
    onSecondaryContainer = tonalPalette.secondary10,
    tertiary = tonalPalette.tertiary40,
    onTertiary = tonalPalette.tertiary100,
    tertiaryContainer = tonalPalette.tertiary90,
    onTertiaryContainer = tonalPalette.tertiary10,
    background = tonalPalette.neutralVariant98,
    onBackground = tonalPalette.neutralVariant10,
    surface = tonalPalette.neutralVariant98,
    onSurface = tonalPalette.neutralVariant10,
    surfaceVariant = tonalPalette.neutralVariant90,
    onSurfaceVariant = tonalPalette.neutralVariant30,
    inverseSurface = tonalPalette.neutralVariant20,
    inverseOnSurface = tonalPalette.neutralVariant95,
    outline = tonalPalette.neutralVariant50,
    outlineVariant = tonalPalette.neutralVariant80,
    scrim = tonalPalette.neutralVariant0,
    surfaceBright = tonalPalette.neutralVariant98,
    surfaceDim = tonalPalette.neutralVariant87,
    surfaceContainer = tonalPalette.neutralVariant94,
    surfaceContainerHigh = tonalPalette.neutralVariant92,
    surfaceContainerHighest = tonalPalette.neutralVariant90,
    surfaceContainerLow = tonalPalette.neutralVariant96,
    surfaceContainerLowest = tonalPalette.neutralVariant100,
    surfaceTint = tonalPalette.primary40,
)

@RequiresApi(34)
internal fun dynamicLightColorScheme34(tonalPalette: TonalPalette) = lightColorScheme(
    primary = tonalPalette.primary40,
    onPrimary = tonalPalette.primary100,
    primaryContainer = tonalPalette.primary90,
    onPrimaryContainer = tonalPalette.primary10,
    inversePrimary = tonalPalette.primary80,
    secondary = tonalPalette.secondary40,
    onSecondary = tonalPalette.secondary100,
    secondaryContainer = tonalPalette.secondary90,
    onSecondaryContainer = tonalPalette.secondary10,
    tertiary = tonalPalette.tertiary40,
    onTertiary = tonalPalette.tertiary100,
    tertiaryContainer = tonalPalette.tertiary90,
    onTertiaryContainer = tonalPalette.tertiary10,
    background = tonalPalette.neutral98,
    onBackground = tonalPalette.neutral10,
    surface = tonalPalette.neutral98,
    onSurface = tonalPalette.neutral10,
    surfaceVariant = tonalPalette.neutralVariant90,
    onSurfaceVariant = tonalPalette.neutralVariant30,
    inverseSurface = tonalPalette.neutral20,
    inverseOnSurface = tonalPalette.neutral95,
    outline = tonalPalette.neutralVariant50,
    outlineVariant = tonalPalette.neutralVariant80,
    scrim = tonalPalette.neutral0,
    surfaceBright = tonalPalette.neutral98,
    surfaceDim = tonalPalette.neutral87,
    surfaceContainer = tonalPalette.neutral94,
    surfaceContainerHigh = tonalPalette.neutral92,
    surfaceContainerHighest = tonalPalette.neutral90,
    surfaceContainerLow = tonalPalette.neutral96,
    surfaceContainerLowest = tonalPalette.neutral100,
    surfaceTint = tonalPalette.primary40,
)

@RequiresApi(31)
internal fun dynamicDarkColorScheme31(tonalPalette: TonalPalette) = darkColorScheme(
    primary = tonalPalette.primary80,
    onPrimary = tonalPalette.primary20,
    primaryContainer = tonalPalette.primary30,
    onPrimaryContainer = tonalPalette.primary90,
    inversePrimary = tonalPalette.primary40,
    secondary = tonalPalette.secondary80,
    onSecondary = tonalPalette.secondary20,
    secondaryContainer = tonalPalette.secondary30,
    onSecondaryContainer = tonalPalette.secondary90,
    tertiary = tonalPalette.tertiary80,
    onTertiary = tonalPalette.tertiary20,
    tertiaryContainer = tonalPalette.tertiary30,
    onTertiaryContainer = tonalPalette.tertiary90,
    background = tonalPalette.neutralVariant6,
    onBackground = tonalPalette.neutralVariant90,
    surface = tonalPalette.neutralVariant6,
    onSurface = tonalPalette.neutralVariant90,
    surfaceVariant = tonalPalette.neutralVariant30,
    onSurfaceVariant = tonalPalette.neutralVariant80,
    inverseSurface = tonalPalette.neutralVariant90,
    inverseOnSurface = tonalPalette.neutralVariant20,
    outline = tonalPalette.neutralVariant60,
    outlineVariant = tonalPalette.neutralVariant30,
    scrim = tonalPalette.neutralVariant0,
    surfaceBright = tonalPalette.neutralVariant24,
    surfaceDim = tonalPalette.neutralVariant6,
    surfaceContainer = tonalPalette.neutralVariant12,
    surfaceContainerHigh = tonalPalette.neutralVariant17,
    surfaceContainerHighest = tonalPalette.neutralVariant22,
    surfaceContainerLow = tonalPalette.neutralVariant10,
    surfaceContainerLowest = tonalPalette.neutralVariant4,
    surfaceTint = tonalPalette.primary80,
)

@RequiresApi(34)
internal fun dynamicDarkColorScheme34(tonalPalette: TonalPalette) = darkColorScheme(
    primary = tonalPalette.primary80,
    onPrimary = tonalPalette.primary20,
    primaryContainer = tonalPalette.primary30,
    onPrimaryContainer = tonalPalette.primary90,
    inversePrimary = tonalPalette.primary40,
    secondary = tonalPalette.secondary80,
    onSecondary = tonalPalette.secondary20,
    secondaryContainer = tonalPalette.secondary30,
    onSecondaryContainer = tonalPalette.secondary90,
    tertiary = tonalPalette.tertiary80,
    onTertiary = tonalPalette.tertiary20,
    tertiaryContainer = tonalPalette.tertiary30,
    onTertiaryContainer = tonalPalette.tertiary90,
    background = tonalPalette.neutral6,
    onBackground = tonalPalette.neutral90,
    surface = tonalPalette.neutral6,
    onSurface = tonalPalette.neutral90,
    surfaceVariant = tonalPalette.neutralVariant30,
    onSurfaceVariant = tonalPalette.neutralVariant80,
    inverseSurface = tonalPalette.neutral90,
    inverseOnSurface = tonalPalette.neutral20,
    outline = tonalPalette.neutralVariant60,
    outlineVariant = tonalPalette.neutral30,
    scrim = tonalPalette.neutral0,
    surfaceBright = tonalPalette.neutral24,
    surfaceDim = tonalPalette.neutral6,
    surfaceContainer = tonalPalette.neutral12,
    surfaceContainerHigh = tonalPalette.neutral17,
    surfaceContainerHighest = tonalPalette.neutral22,
    surfaceContainerLow = tonalPalette.neutral10,
    surfaceContainerLowest = tonalPalette.neutral4,
    surfaceTint = tonalPalette.primary80,
)