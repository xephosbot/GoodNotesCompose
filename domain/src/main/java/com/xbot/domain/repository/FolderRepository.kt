package com.xbot.domain.repository

import com.xbot.domain.model.FolderModel
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    val folders: Flow<List<FolderModel>>

    suspend fun insertFolder(folder: FolderModel)

    suspend fun deleteFolder(folder: FolderModel)

    suspend fun updateFolder(positionFrom: Int, positionTo: Int)
}