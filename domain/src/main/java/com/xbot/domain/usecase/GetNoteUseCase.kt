package com.xbot.domain.usecase

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): NoteModel? {
        return repository.getNote(noteId)
    }
}