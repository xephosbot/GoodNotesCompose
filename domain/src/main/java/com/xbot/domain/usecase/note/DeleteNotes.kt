package com.xbot.domain.usecase.note

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNotes @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(notes: List<NoteModel>, folderId: Long, actionId: Long) {
        repository.deleteNotes(notes, folderId, actionId)
    }
}