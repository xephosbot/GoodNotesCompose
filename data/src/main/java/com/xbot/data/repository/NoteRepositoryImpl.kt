package com.xbot.data.repository

import com.xbot.data.mapToDataModel
import com.xbot.data.mapToDomainModel
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.model.note.NoteEntity
import com.xbot.data.model.note.NoteUpdate
import com.xbot.data.source.FolderDao
import com.xbot.data.source.NoteDao
import com.xbot.data.source.NoteFolderCrossRefDao
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao,
    private val folderDao: FolderDao,
    private val noteFolderCrossRefDao: NoteFolderCrossRefDao
) : NoteRepository {

    private val currentFolderId = MutableStateFlow(DEFAULT_FOLDER_ID)

    private val deletedItems = mutableMapOf<Long, DeletedItems>()

    override val notes: Flow<List<NoteModel>> = currentFolderId.flatMapLatest { folderId ->
        val notesFlow = when (folderId) {
            DEFAULT_FOLDER_ID -> noteDao.getNotes().map { it.map(NoteEntity::mapToDomainModel) }
            else -> folderDao.getFolderWithNotes(folderId).map { it.notes.map(NoteEntity::mapToDomainModel) }
        }
        notesFlow.map { noteList ->
            noteList.sortedByDescending { it.timeStamp }
        }
    }

    override suspend fun getNote(noteId: Long): NoteModel? {
        return noteDao.getNote(noteId)?.mapToDomainModel()
    }

    override suspend fun insertNote(note: NoteModel, folderId: Long) {
        val noteId = noteDao.insert(note.mapToDataModel())
        if (folderId != DEFAULT_FOLDER_ID) {
            val crossRef = NoteFolderCrossRef(noteId, folderId)
            noteFolderCrossRefDao.insert(crossRef)
        }
    }

    override suspend fun deleteNotes(notes: List<NoteModel>, folderId: Long, actionId: Long) {
        val dataNotes = when (folderId) {
            DEFAULT_FOLDER_ID -> notes.map(NoteModel::mapToDataModel)
            else -> emptyList()
        }

        val crossRefs = when (folderId) {
            DEFAULT_FOLDER_ID -> notes.flatMap { note -> noteFolderCrossRefDao.getCrossRefsForNote(note.id) }
            else -> notes.map { note -> NoteFolderCrossRef(note.id, folderId) }
        }

        deletedItems[actionId] = DeletedItems(notes = dataNotes, crossRefs = crossRefs)

        when (folderId) {
            DEFAULT_FOLDER_ID -> noteDao.deleteAll(dataNotes)
            else -> noteFolderCrossRefDao.deleteAll(crossRefs)
        }
    }

    override suspend fun restoreNotes(actionId: Long) {
        deletedItems[actionId]?.let {
            noteDao.insertAll(it.notes)
            noteFolderCrossRefDao.insertAll(it.crossRefs)
        }
    }

    override suspend fun updateNote(noteId: Long, isFavorite: Boolean) {
        val update = NoteUpdate(noteId, isFavorite)
        noteDao.update(update)
    }

    override suspend fun openFolder(folderId: Long) {
        currentFolderId.update { folderId }
    }

    private data class DeletedItems(
        val notes: List<NoteEntity>,
        val crossRefs: List<NoteFolderCrossRef>
    )

    companion object {
        private const val DEFAULT_FOLDER_ID = 0L
    }
}
