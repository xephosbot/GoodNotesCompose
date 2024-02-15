package com.xbot.goodnotes

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
    Note(id, title, content, isFavorite, timeStamp.convertToDateTime(), colorId)

internal fun FolderModel.mapToUIModel() =
    Folder(id, name, noteCount)

internal fun Note.mapToDomainModel() =
    NoteModel(id, title, content, isFavorite, 0L, colorId)

internal fun Long.convertToDateTime(): String {
    val instant = Instant.fromEpochSeconds(this)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return DateTimeFormatter
        .ofPattern("dd MMM yyyy")
        .format(localDateTime.toJavaLocalDateTime())
}
