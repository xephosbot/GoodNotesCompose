package com.xbot.domain.usecase.note

import com.xbot.domain.model.NoteModel
import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotes @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<NoteModel>> {
        return repository.notes
    }
}
