package com.xbot.domain.usecase.note

import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long, isFavorite: Boolean) {
        repository.updateNote(noteId, isFavorite)
    }
}