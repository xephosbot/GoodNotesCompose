package com.xbot.goodnotes.ui.feature.note

import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NoteScreenState(
    val notesList: ImmutableList<Note> = persistentListOf(),
    val foldersList: ImmutableList<Folder> = persistentListOf(),
    val currentFolderId: Long = 0L
)

sealed interface NoteScreenAction {
    data class OpenFolder(val folderId: Long): NoteScreenAction
    data class AddFolder(val name: String): NoteScreenAction
    data class UpdateNote(val note: Note, val isFavorite: Boolean): NoteScreenAction
    data class DeleteNotes(val notes: List<Note>): NoteScreenAction
    data object UndoDelete : NoteScreenAction
}
