package com.xbot.domain.model

data class NoteModel(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val timeStamp: Long,
    val colorId: Int
)
