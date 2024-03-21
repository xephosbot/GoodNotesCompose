package com.xbot.domain.usecase.note

import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteCount @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.noteCount
    }
}