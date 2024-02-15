package com.xbot.domain.usecase

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: NoteModel, folderId: Long = 0L) {
        repository.deleteNote(note, folderId)
    }
}