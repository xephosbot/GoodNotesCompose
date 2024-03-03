package com.xbot.domain.usecase.folder

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import javax.inject.Inject

class DeleteFolder @Inject constructor(
    private val repository: FolderRepository
) {
    suspend operator fun invoke(folder: FolderModel) {
        repository.deleteFolder(folder)
    }
}