package com.xbot.domain.usecase

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(folder: FolderModel) {
        repository.deleteFolder(folder)
    }
}