package com.xbot.domain.repository

import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    val notes: Flow<List<NoteModel>>

    suspend fun getNote(noteId: Long): NoteModel?

    suspend fun addNote(note: NoteModel, folderId: Long = 0L)

    suspend fun deleteNote(note: NoteModel, folderId: Long = 0L)

    val folders: Flow<List<FolderModel>>

    suspend fun openFolder(folderId: Long)

    suspend fun addFolder(folder: FolderModel)

    suspend fun deleteFolder(folder: FolderModel)
}