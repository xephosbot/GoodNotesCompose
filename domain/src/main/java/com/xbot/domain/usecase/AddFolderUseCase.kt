package com.xbot.domain.usecase

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(
    private val repository: FolderRepository
) {
    suspend operator fun invoke(folder: FolderModel) {
        repository.insertFolder(folder)
    }
}