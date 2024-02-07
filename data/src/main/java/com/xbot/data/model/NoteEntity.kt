package com.xbot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Clock

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val noteId: Long,
    val title: String,
    val content: String,
    val timeStamp: Long = Clock.System.now().toEpochMilliseconds(),
    val colorId: Int
)