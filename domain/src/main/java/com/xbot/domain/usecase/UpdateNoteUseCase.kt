package com.xbot.domain.usecase

import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long, isFavorite: Boolean) {
        repository.updateNote(noteId, isFavorite)
    }
}