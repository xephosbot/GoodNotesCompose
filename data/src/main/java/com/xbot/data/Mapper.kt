package com.xbot.data

import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.note.NoteEntity
import com.xbot.domain.model.AppTheme
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel

internal fun NoteEntity.mapToDomainModel() =
    NoteModel(noteId, title, content, isFavorite, timeStamp, colorId)

internal fun FolderEntity.mapToDomainModel(noteCount: Int) =
    FolderModel(folderId, name, noteCount)

internal fun NoteModel.mapToDataModel() =
    NoteEntity(id, title, content, isFavorite, timeStamp, colorId)

internal fun FolderModel.mapToDataModel() =
    FolderEntity(id, name)

internal fun Int.toAppTheme() = when(this) {
    0 -> AppTheme.System
    1 -> AppTheme.Light
    2 -> AppTheme.Dark
    else -> AppTheme.System
}

internal fun AppTheme.toInt() = when(this) {
    is AppTheme.System -> 0
    is AppTheme.Light -> 1
    is AppTheme.Dark -> 2
}
