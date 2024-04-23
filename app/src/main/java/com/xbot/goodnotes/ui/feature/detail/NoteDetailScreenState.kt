package com.xbot.goodnotes.ui.feature.detail

data class NoteDetailScreenState(
    val noteId: Long = -1L,
    val noteTitle: String = "",
    val noteText: String = "",
    val noteColorId: Int = 0,
    val noteIsFavorite: Boolean = false
)

sealed interface NoteDetailScreenAction {
    data class UpdateNote(val isFavorite: Boolean): NoteDetailScreenAction
    data object Save : NoteDetailScreenAction
}
