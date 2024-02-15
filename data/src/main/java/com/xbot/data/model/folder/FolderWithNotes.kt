package com.xbot.data.model.folder

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.model.note.NoteEntity

data class FolderWithNotes(
    @Embedded val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "noteId",
        associateBy = Junction(NoteFolderCrossRef::class)
    )
    val notes: List<NoteEntity>
)