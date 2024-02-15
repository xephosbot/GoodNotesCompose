package com.xbot.data.repository

import android.content.Context
import android.util.Log
import com.xbot.data.R
import com.xbot.data.mapToDataModel
import com.xbot.data.mapToDomainModel
import com.xbot.data.model.folder.FolderUpdate
import com.xbot.data.prepend
import com.xbot.data.source.FolderDao
import com.xbot.data.source.NoteDao
import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FolderRepositoryImpl @Inject constructor(
    private val context: Context,
    private val noteDao: NoteDao,
    private val folderDao: FolderDao
) : FolderRepository {

    override val folders: Flow<List<FolderModel>> = folderDao.getFoldersWithNoteCount().map {
        it.map { (folder, noteCount) ->
            folder.mapToDomainModel(noteCount)
        }.prepend(FolderModel(0L, context.getString(R.string.data_folder_all_title), noteDao.getNoteCount()))
    }

    override suspend fun insertFolder(folder: FolderModel) {
        folderDao.insert(folder.mapToDataModel())
    }

    override suspend fun deleteFolder(folder: FolderModel) {
        folderDao.delete(folder.mapToDataModel())
    }

    override suspend fun updateFolder(positionFrom: Int, positionTo: Int) {
        Log.e("TAGGGGGGGGGGG", "from index: $positionFrom, to index: $positionTo")

        val folderFrom = folderDao.getFolder(positionFrom)!!
        val folderTo = folderDao.getFolder(positionTo)!!
        val update1 = FolderUpdate(folderFrom.folderId, positionTo)
        val update2 = FolderUpdate(folderTo.folderId, positionFrom)
        folderDao.updates(listOf(update2, update1))
    }
}
