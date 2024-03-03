package com.xbot.domain.usecase.note

import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class RestoreNotes @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(actionId: Long) {
        repository.restoreNotes(actionId)
    }
}