package com.xbot.domain.usecase.folder

import com.xbot.domain.model.FolderModel
import com.xbot.domain.repository.FolderRepository
import javax.inject.Inject

class UpdateFolders @Inject constructor(
    private val repository: FolderRepository
) {
    operator fun invoke(folders: List<FolderModel>) {
        repository.updateFolder(folders)
    }
}
