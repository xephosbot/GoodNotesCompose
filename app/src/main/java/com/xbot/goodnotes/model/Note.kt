package com.xbot.goodnotes.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val isFavorite: Boolean,
    val timeStamp: Long,
    val colorId: Int
) : Parcelable
