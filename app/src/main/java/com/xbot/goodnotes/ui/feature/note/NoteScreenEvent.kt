package com.xbot.goodnotes.ui.feature.note

import com.xbot.goodnotes.model.Note
import kotlinx.collections.immutable.ImmutableList

sealed interface NoteScreenEvent {
    data class OpenFolder(val folderId: Long) : NoteScreenEvent
    data class AddFolder(val name: String) : NoteScreenEvent
    data class DeleteNotes(val notes: ImmutableList<Note>) : NoteScreenEvent
}