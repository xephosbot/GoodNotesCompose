package com.xbot.goodnotes

import com.xbot.domain.model.AppTheme
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

internal fun NoteModel.mapToUIModel() =
    Note(id, title, content, isFavorite, timeStamp, colorId)

internal fun FolderModel.mapToUIModel() =
    Folder(id, name, order)

internal fun Note.mapToDomainModel() =
    NoteModel(id, title, content, isFavorite, timeStamp, colorId)

internal fun Folder.mapToDomainModel() =
    FolderModel(id, name, order)

internal fun AppTheme.toInt() = when (this) {
    is AppTheme.System -> 0
    is AppTheme.Light -> 1
    is AppTheme.Dark -> 2
}

internal fun Int.toAppTheme() = when (this) {
    0 -> AppTheme.System
    1 -> AppTheme.Light
    2 -> AppTheme.Dark
    else -> AppTheme.System
}

internal fun Long.convertToDateTime(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return DateTimeFormatter
        .ofPattern("dd MMM yyyy")
        .format(localDateTime.toJavaLocalDateTime())
}
