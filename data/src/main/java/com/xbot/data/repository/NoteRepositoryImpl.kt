package com.xbot.data.repository

import android.content.Context
import com.xbot.data.R
import com.xbot.data.mapToDataModel
import com.xbot.data.mapToDomainModel
import com.xbot.data.model.FolderWithNoteCount
import com.xbot.data.model.NoteEntity
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.prepend
import com.xbot.data.source.NoteDao
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val context: Context,
    private val noteDao: NoteDao
) : NoteRepository {

    private val currentFolderId = MutableStateFlow(DEFAULT_FOLDER_ID)

    override val notes: Flow<List<NoteModel>> = currentFolderId.flatMapLatest { folderId ->
        if (folderId == DEFAULT_FOLDER_ID) {
            noteDao.getNotes().map { it.map(NoteEntity::mapToDomainModel) }
        } else {
            noteDao.getFolderWithNotes(folderId).map { it.notes.map(NoteEntity::mapToDomainModel) }
        }
    }

    override suspend fun getNote(noteId: Long): NoteModel? {
        return noteDao.getNote(noteId)?.mapToDomainModel()
    }

    override suspend fun addNote(note: NoteModel, folderId: Long) {
        val noteId = noteDao.insertNote(note.mapToDataModel())
        if (folderId != DEFAULT_FOLDER_ID) {
            val crossRef = NoteFolderCrossRef(noteId, folderId)
            noteDao.insertNoteFolderCrossRef(crossRef)
        }
    }

    override suspend fun deleteNote(note: NoteModel, folderId: Long) {
        if (folderId == DEFAULT_FOLDER_ID) {
            noteDao.deleteNote(note.mapToDataModel())
        } else {
            val crossRef = NoteFolderCrossRef(note.id, folderId)
            noteDao.deleteNoteFolderCrossRef(crossRef)
        }
    }

    override val folders: Flow<List<FolderModel>> = noteDao.getFolders().map {
        it.map(FolderWithNoteCount::mapToDomainModel).prepend(
            FolderModel(0L, context.getString(R.string.data_folder_all_title), noteDao.getNoteCount())
        )
    }

    override suspend fun openFolder(folderId: Long) {
        currentFolderId.update { folderId }
    }

    override suspend fun addFolder(folder: FolderModel) {
        noteDao.insertFolder(folder.mapToDataModel())
    }

    override suspend fun deleteFolder(folder: FolderModel) {
        noteDao.deleteFolder(folder.mapToDataModel())
    }

    companion object {
        private const val DEFAULT_FOLDER_ID = 0L
    }
}
