package com.xbot.goodnotes.ui.feature.note

import com.xbot.common.Constants
import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NoteScreenState(
    val notesList: ImmutableList<Note> = persistentListOf(),
    val foldersList: ImmutableList<Folder> = persistentListOf(),
    val relatedFolders: ImmutableList<Folder> = persistentListOf(),
    val noteCount: Int = 0,
    val currentFolderId: Long = Constants.DEFAULT_FOLDER_ID
)

sealed interface NoteScreenAction {
    data class OpenFolder(val folderId: Long) : NoteScreenAction
    data class AddFolder(val name: String) : NoteScreenAction
    data class UpdateNote(val note: Note, val isFavorite: Boolean) : NoteScreenAction
    data class DeleteNotes(val notes: List<Note>) : NoteScreenAction
    data class DeleteFolder(val folder: Folder) : NoteScreenAction
    data class UpdateRelatedFolders(val notes: List<Note>) : NoteScreenAction
    data class ChangeFolderForNotes(val notes: List<Note>, val folder: Folder, val value: Boolean) : NoteScreenAction
    data class UpdateFolders(val folders: List<Folder>) : NoteScreenAction
}
