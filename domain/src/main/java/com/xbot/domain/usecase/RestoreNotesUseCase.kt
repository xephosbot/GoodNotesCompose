package com.xbot.domain.usecase

import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class RestoreNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke() {
        repository.restoreNotes()
    }
}