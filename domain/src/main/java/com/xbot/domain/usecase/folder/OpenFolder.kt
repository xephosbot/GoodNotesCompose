package com.xbot.domain.usecase.folder

import com.xbot.domain.repository.NoteRepository
import javax.inject.Inject

class OpenFolder @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(folderId: Long) {
        repository.openFolder(folderId)
    }
}