package com.xbot.data.repository

import com.xbot.data.dao.FolderDao
import com.xbot.data.mapToDataModel
import com.xbot.data.mapToDomainModel
import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.folder.FolderUpdate
import com.xbot.data.model.note.NoteEntity
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderRepository {

    override val folders: Flow<List<FolderModel>> = folderDao.getFolders().map {
        it.map(FolderEntity::mapToDomainModel)
    }

    override fun getNotesFromFolder(folderId: Long): Flow<List<NoteModel>> {
        return folderDao.getFolderWithNotes(folderId).map { it.notes.map(NoteEntity::mapToDomainModel) }
    }

    override suspend fun insertFolder(folder: FolderModel) {
        val order = folderDao.getMaxOrder() + 1
        folderDao.insert(folder.copy(order = order).mapToDataModel())
    }

    override suspend fun deleteFolder(folder: FolderModel) {
        folderDao.delete(folder.mapToDataModel())
    }

    override fun updateFolder(folders: List<FolderModel>) {
        val update = folders.mapIndexed { index, folder ->
            FolderUpdate(folder.id, index)
        }
        folderDao.update(*update.toTypedArray())
    }
}
