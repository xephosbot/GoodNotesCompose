package com.xbot.data.model.folder

import androidx.room.Entity

@Entity
data class FolderUpdate(
    val folderId: Long,
    val position: Int
)
