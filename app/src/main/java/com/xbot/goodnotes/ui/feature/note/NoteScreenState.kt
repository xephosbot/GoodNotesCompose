package com.xbot.goodnotes.ui.feature.note

import com.xbot.goodnotes.model.Folder
import com.xbot.goodnotes.model.Note
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NoteScreenState(
    val notesList: ImmutableList<Note> = persistentListOf(),
    val foldersList: ImmutableList<Folder> = persistentListOf(),
    val currentFolderId: Long = 0L
)
