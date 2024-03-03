package com.xbot.goodnotes.ui.feature.detail

data class NoteDetailScreenState(
    val noteId: Long = -1L,
    val noteTitle: String = "",
    val noteText: String = "",
    val noteColorId: Int = 0
)

sealed interface NoteDetailScreenAction {
    data object Save : NoteDetailScreenAction
}
