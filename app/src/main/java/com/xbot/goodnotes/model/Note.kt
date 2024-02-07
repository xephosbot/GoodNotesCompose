package com.xbot.goodnotes.model

import androidx.compose.runtime.Stable

@Stable
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val dateTime: String,
    val colorId: Int
)
