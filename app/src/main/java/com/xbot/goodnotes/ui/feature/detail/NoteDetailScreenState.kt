package com.xbot.goodnotes.ui.feature.detail

import com.xbot.common.Constants
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class NoteDetailScreenState(
    val noteId: Long = Constants.NEW_NOTE_ID,
    val noteTitle: String = "",
    val noteText: String = "",
    val noteIsFavorite: Boolean = false,
    val noteTimestamp: Long = kotlin.time.Clock.System.now().toEpochMilliseconds() / 1000,
    val noteColorId: Int = 0,
)

sealed interface NoteDetailScreenAction {
    data class UpdateNote(val isFavorite: Boolean) : NoteDetailScreenAction
    data class ChangeNoteColor(val colorId: Int) : NoteDetailScreenAction
    data object Save : NoteDetailScreenAction
}
