package com.xbot.domain.usecase.folder

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class GetFoldersRelatedToNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): List<FolderModel> {
        return repository.getFoldersRelatedToNote(noteId)
    }
}