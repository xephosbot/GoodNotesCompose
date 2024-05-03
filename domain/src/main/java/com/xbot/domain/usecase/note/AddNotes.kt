package com.xbot.domain.usecase.note

import com.xbot.common.Constants
import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class AddNotes @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(notes: List<NoteModel>, folderId: Long = Constants.DEFAULT_FOLDER_ID) {
        notes.forEach { note ->
            if (note.title.isNotBlank() && note.content.isNotBlank()) {
                repository.insertNote(note, folderId)
            }
        }
    }
}