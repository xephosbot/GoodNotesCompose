package com.xbot.domain.usecase.folder

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFolders @Inject constructor(
    private val repository: FolderRepository
) {
    operator fun invoke(): Flow<List<FolderModel>> {
        return repository.folders.map { folderList ->
            folderList.sortedBy { it.order }
        }
    }
}