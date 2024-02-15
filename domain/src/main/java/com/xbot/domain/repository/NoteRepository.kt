package com.xbot.domain.repository

import com.xbot.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val notes: Flow<List<NoteModel>>

    suspend fun getNote(noteId: Long): NoteModel?

    suspend fun insertNote(note: NoteModel, folderId: Long = 0L)

    suspend fun deleteNote(note: NoteModel, folderId: Long = 0L)

    suspend fun updateNote(noteId: Long, isFavorite: Boolean)

    suspend fun openFolder(folderId: Long)
}