package com.xbot.domain.di

import com.xbot.domain.usecase.FolderUseCase
import com.xbot.domain.usecase.NoteUseCase
import com.xbot.domain.usecase.folder.AddFolder
import com.xbot.domain.usecase.folder.DeleteFolder
import com.xbot.domain.usecase.folder.GetFolders
import com.xbot.domain.usecase.folder.OpenFolder
import com.xbot.domain.usecase.note.AddNote
import com.xbot.domain.usecase.note.DeleteNotes
import com.xbot.domain.usecase.note.GetNote
import com.xbot.domain.usecase.note.GetNotes
import com.xbot.domain.usecase.note.RestoreNotes
import com.xbot.domain.usecase.note.UpdateNote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideNoteUseCase(
        addNote: AddNote,
        deleteNotes: DeleteNotes,
        getNote: GetNote,
        getNotes: GetNotes,
        restoreNotes: RestoreNotes,
        updateNote: UpdateNote
    ): NoteUseCase {
        return NoteUseCase(
            addNote = addNote,
            deleteNotes = deleteNotes,
            getNote = getNote,
            getNotes = getNotes,
            restoreNotes = restoreNotes,
            updateNote = updateNote
        )
    }

    @Provides
    fun provideFolderUseCase(
        addFolder: AddFolder,
        deleteFolder: DeleteFolder,
        getFolders: GetFolders,
        openFolder: OpenFolder
    ): FolderUseCase {
        return FolderUseCase(
            addFolder = addFolder,
            deleteFolder = deleteFolder,
            getFolders = getFolders,
            openFolder = openFolder
        )
    }
}