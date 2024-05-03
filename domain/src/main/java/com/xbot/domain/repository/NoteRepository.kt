package com.xbot.domain.repository

import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val notes: Flow<List<NoteModel>>

    suspend fun getNote(noteId: Long): NoteModel?

    suspend fun getFoldersRelatedToNote(noteId: Long): List<FolderModel>

    suspend fun insertNote(note: NoteModel, folderId: Long = 0L)

    suspend fun deleteNotes(notes: List<NoteModel>, folderId: Long = 0L, actionId: Long)

    suspend fun restoreNotes(actionId: Long)

    suspend fun updateNote(noteId: Long, isFavorite: Boolean)
}