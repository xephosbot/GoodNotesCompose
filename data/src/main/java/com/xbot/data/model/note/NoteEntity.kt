package com.xbot.data.model.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val noteId: Long,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val timeStamp: Long = Clock.System.now().toEpochMilliseconds(),
    val colorId: Int
)