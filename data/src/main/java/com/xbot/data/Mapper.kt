package com.xbot.data

import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.note.NoteEntity
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
