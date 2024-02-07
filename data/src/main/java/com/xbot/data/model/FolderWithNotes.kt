package com.xbot.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FolderWithNotes(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "noteId",
        associateBy = Junction(NoteFolderCrossRef::class)
    )
    val notes: List<NoteEntity>
)