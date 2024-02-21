package com.xbot.ui.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.lerp
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.style.lerp
import androidx.compose.ui.unit.fontscaling.FontScaleConverter
import androidx.compose.ui.unit.fontscaling.FontScaleConverterFactory

internal fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

internal fun lerp(start: Int, stop: Int, fraction: Float): Int {
    return (start + fraction * (stop - start)).toInt()
}

internal fun TextStyle.blend(start: TextStyle, stop: TextStyle, fraction: Float): TextStyle {
    return this.copy(
        fontWeight = lerp(
            start.fontWeight ?: FontWeight.Normal,
            stop.fontWeight ?: FontWeight.Normal,
            fraction
        ),
        letterSpacing = stop.letterSpacing,
        baselineShift = lerp(
            start.baselineShift ?: BaselineShift(0f),
            stop.baselineShift ?: BaselineShift(0f),
            fraction
        ),
        textGeometricTransform = lerp(
            start.textGeometricTransform ?: TextGeometricTransform(1.0f, 0.0f),
            stop.textGeometricTransform ?: TextGeometricTransform(1.0f, 0.0f),
            fraction
        ),
        background = lerp(
            start.background,
            stop.background,
            fraction
        ),
        shadow = lerp(
            start.shadow ?: Shadow(),
            stop.shadow ?: Shadow(),
            fraction
        ),
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.Both
        )
    )
}

@SuppressLint("RestrictedApi")
@Composable
fun rememberFontScaleConverter(): FontScaleConverter? {
    val density = LocalDensity.current
    return remember(density) {
        FontScaleConverterFactory.forScale(density.fontScale)
    }
}
