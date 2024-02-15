package com.xbot.data.model.note

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.model.folder.FolderEntity

data class NoteWithFolders(
    @Embedded val note: NoteEntity,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "folderId",
        associateBy = Junction(NoteFolderCrossRef::class)
    )
    val folders: List<FolderEntity>
)
