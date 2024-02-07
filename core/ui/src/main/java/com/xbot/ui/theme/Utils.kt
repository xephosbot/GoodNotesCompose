package com.xbot.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.material.color.MaterialColors
import kotlin.math.ln


fun ColorScheme.adjustColorAtElevation(
    color: Color,
    elevation: Dp,
): Color {
    if (elevation == 0.dp) return color
    val alpha = ((14.5f * ln(elevation.value + 1)) + 2f) / 100f
    return surfaceTint.copy(alpha = alpha).compositeOver(color)
}

fun Color.harmonize(with: Color) = Color(MaterialColors.harmonize(this.toArgb(), with.toArgb()))

val Color.harmonized
    @Composable
    get() = harmonize(MaterialTheme.colorScheme.primary)

