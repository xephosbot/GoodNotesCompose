package com.xbot.goodnotes.ui.feature.note

import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import kotlinx.collections.immutable.ImmutableList

sealed interface NoteScreenEvent {
    data class OpenFolder(val folderId: Long) : NoteScreenEvent
    data class AddFolder(val name: String) : NoteScreenEvent
    data class UpdateNote(val note: Note, val isFavorite: Boolean): NoteScreenEvent
    data class UpdateFolder(val positionFrom: Int, val positionTo: Int): NoteScreenEvent
    data class DeleteNotes(val notes: List<Note>) : NoteScreenEvent
}