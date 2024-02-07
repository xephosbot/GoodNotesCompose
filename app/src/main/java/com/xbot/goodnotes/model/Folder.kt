package com.xbot.goodnotes.model

import androidx.compose.runtime.Stable

@Stable
data class Folder(
    val id: Long = 0L,
    val name: String,
    val noteCount: Int
)
