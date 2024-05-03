package com.xbot.domain.usecase.folder

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import javax.inject.Inject

class AddFolder @Inject constructor(
    private val repository: FolderRepository
) {
    suspend operator fun invoke(folder: FolderModel) {
        if (folder.name.isNotBlank()) {
            repository.insertFolder(folder)
        }
    }
}