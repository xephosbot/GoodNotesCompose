package com.xbot.domain.usecase

import com.xbot.domain.usecase.folder.AddFolder
import com.xbot.domain.usecase.folder.DeleteFolder
import com.xbot.domain.usecase.folder.GetFolders
import com.xbot.domain.usecase.folder.OpenFolder

data class FolderUseCase(
    val addFolder: AddFolder,
    val deleteFolder: DeleteFolder,
    val getFolders: GetFolders,
    val openFolder: OpenFolder
)
