package com.xbot.data

import com.xbot.data.model.FolderEntity
import com.xbot.data.model.FolderWithNoteCount
import com.xbot.data.model.NoteEntity
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel

internal fun NoteEntity.mapToDomainModel() =
    NoteModel(noteId, title, content, timeStamp, colorId)

internal fun FolderWithNoteCount.mapToDomainModel() =
    FolderModel(folder.folderId, folder.name, noteCount)

internal fun NoteModel.mapToDataModel() =
    NoteEntity(id, title, content, timeStamp, colorId)

internal fun FolderModel.mapToDataModel() =
    FolderEntity(id, name)
