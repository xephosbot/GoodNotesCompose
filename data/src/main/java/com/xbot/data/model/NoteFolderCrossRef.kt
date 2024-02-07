package com.xbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "note_folder",
    primaryKeys = ["noteId", "folderId"],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["noteId"],
            childColumns = ["noteId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["folderId"],
            childColumns = ["folderId"],
            onDelete = CASCADE
        )
    ]
)
data class NoteFolderCrossRef(
    val noteId: Long,
    @ColumnInfo(index = true) val folderId: Long
)
