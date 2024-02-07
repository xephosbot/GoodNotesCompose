package com.xbot.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NoteWithFolders(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "folderId",
        associateBy = Junction(NoteFolderCrossRef::class)
    )
    val folders: List<FolderEntity>
)
