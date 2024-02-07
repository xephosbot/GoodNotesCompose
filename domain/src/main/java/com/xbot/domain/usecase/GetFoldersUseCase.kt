package com.xbot.domain.usecase

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoldersUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<FolderModel>> {
        return repository.folders
    }
}