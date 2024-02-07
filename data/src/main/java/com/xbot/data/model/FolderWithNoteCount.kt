package com.xbot.data.model

import androidx.room.Embedded

data class FolderWithNoteCount(
    @Embedded val folder: FolderEntity,
    val noteCount: Int
)
