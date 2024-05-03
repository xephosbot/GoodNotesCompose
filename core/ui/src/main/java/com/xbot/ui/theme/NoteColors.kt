package com.xbot.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LightNoteColors = listOf(
    Color(0xFFFEF6B7),
    Color(0xFFF9ACA6),
    Color(0xFFB4DAD2),
    Color(0xFFA8D672),
    Color(0xFFE8E1D1)
)
val DarkNoteColors = listOf(
    Color(0xFF7A531B),
    Color(0xFF77152D),
    Color(0xFF236176),
    Color(0xFF264D3B),
    Color(0xFF4A4139)
)

@Stable
data class NoteColors(
    val value: List<Color> = emptyList()
) {
    operator fun get(index: Int): Color {
        return value[index]
    }
}

val LocalNoteColors = compositionLocalOf { NoteColors() }