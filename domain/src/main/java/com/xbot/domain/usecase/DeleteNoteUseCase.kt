package com.xbot.domain.usecase

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(notes: List<NoteModel>, folderId: Long = 0L) {
        notes.forEach { repository.deleteNote(it, folderId) }
    }
}