package com.xbot.domain.repository

import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    val folders: Flow<List<FolderModel>>

    fun getNotesFromFolder(folderId: Long): Flow<List<NoteModel>>

    suspend fun insertFolder(folder: FolderModel)

    suspend fun deleteFolder(folder: FolderModel)
}