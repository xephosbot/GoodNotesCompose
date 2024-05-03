package com.xbot.data.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val noteId: Long,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val timeStamp: Long,
    val colorId: Int
)