package com.xbot.domain.usecase.note

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class GetNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): NoteModel? {
        return repository.getNote(noteId)
    }
}