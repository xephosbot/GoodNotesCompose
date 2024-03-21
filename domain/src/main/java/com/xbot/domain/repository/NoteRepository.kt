package com.xbot.domain.repository

import com.xbot.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val notes: Flow<List<NoteModel>>

    val noteCount: Flow<Int>

    suspend fun getNote(noteId: Long): NoteModel?

    suspend fun insertNote(note: NoteModel, folderId: Long = 0L)

    suspend fun deleteNotes(notes: List<NoteModel>, folderId: Long = 0L, actionId: Long)

    suspend fun restoreNotes(actionId: Long)

    suspend fun updateNote(noteId: Long, isFavorite: Boolean)
}