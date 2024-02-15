package com.xbot.domain.usecase

import com.xbot.domain.repository.FolderRepository
import javax.inject.Inject

class UpdateFolderUseCase @Inject constructor(
    private val repository: FolderRepository
) {
    suspend operator fun invoke(positionFrom: Int, positionTo: Int) {
        repository.updateFolder(positionFrom, positionTo)
    }
}