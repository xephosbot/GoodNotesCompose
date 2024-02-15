package com.xbot.data.model.note

import androidx.room.Entity

@Entity
data class NoteUpdate(
    val noteId: Long,
    val isFavorite: Boolean
)
