package com.xbot.domain.usecase.note

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class AddNote @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: NoteModel, folderId: Long = 0L) {
        repository.insertNote(note, folderId)
    }
}