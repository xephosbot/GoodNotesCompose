package com.xbot.goodnotes.model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class Folder(
    val id: Long = 0L,
    val name: String,
    val order: Int = 0
) : Parcelable
