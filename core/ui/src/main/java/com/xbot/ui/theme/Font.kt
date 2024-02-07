package com.xbot.ui.theme

import android.os.Build
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.xbot.ui.R

private val MontserratStaticFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_thin,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.montserrat_thinitalic,
        weight = FontWeight.Thin,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_extralight,
        weight = FontWeight.ExtraLight
    ),
    Font(
        resId = R.font.montserrat_extralightitalic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.montserrat_lightitalic,
        weight = FontWeight.Light,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.montserrat_regularitalic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = R.font.montserrat_mediumitalic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = R.font.montserrat_semibolditalic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = R.font.montserrat_bolditalic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_extrabold,
        weight = FontWeight.ExtraBold
    ),
    Font(
        resId = R.font.montserrat_extrabolditalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic
    ),
    Font(
        resId = R.font.montserrat_black,
        weight = FontWeight.Black
    ),
    Font(
        resId = R.font.montserrat_blackitalic,
        weight = FontWeight.Black,
        style = FontStyle.Italic
    )
)

@OptIn(ExperimentalTextApi::class)
private val MontserratVariableFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Thin,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(100),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Thin,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(100),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.ExtraLight,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(200),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(200),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Light,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(300),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Light,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(300),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(400),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Medium,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(500),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.SemiBold,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(600),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(700),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Bold,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(700),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(800),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(800),
            FontVariation.italic(1.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variable,
        weight = FontWeight.Black,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(900),
            FontVariation.italic(0.0f)
        )
    ),
    Font(
        resId = R.font.montserrat_variableitalic,
        weight = FontWeight.Black,
        style = FontStyle.Italic,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(900),
            FontVariation.italic(1.0f)
        )
    )
)

val MontserratFontFamily = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    MontserratVariableFontFamily
} else {
    MontserratStaticFontFamily
}