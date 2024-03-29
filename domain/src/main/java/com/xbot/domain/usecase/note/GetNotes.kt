package com.xbot.domain.usecase.note

import com.xbot.common.Constants
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.FolderRepository
import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNotes @Inject constructor(
    private val noteRepository: NoteRepository,
    private val folderRepository: FolderRepository
) {
    operator fun invoke(folderId: Long): Flow<List<NoteModel>> {
        return when (folderId) {
            Constants.DEFAULT_FOLDER_ID -> noteRepository.notes
            Constants.FAVORITE_FOLDER_ID -> noteRepository.notes.map { noteList ->
                noteList.filter { it.isFavorite }
            }
            else -> folderRepository.getNotesFromFolder(folderId)
        }.map { noteList ->
            noteList.sortedByDescending { it.timeStamp }
        }
    }
}
