package com.xbot.domain.usecase.note

import com.xbot.common.Constants
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: NoteModel, folderId: Long = Constants.DEFAULT_FOLDER_ID) {
        if (note.title.isNotBlank() && note.content.isNotBlank()) {
            repository.insertNote(note, folderId)
        }
    }
}