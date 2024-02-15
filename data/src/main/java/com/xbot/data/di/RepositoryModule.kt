package com.xbot.data.di

import android.content.Context
import com.xbot.data.repository.FolderRepositoryImpl
import com.xbot.data.repository.NoteRepositoryImpl
import com.xbot.data.source.FolderDao
import com.xbot.data.source.NoteDao
import com.xbot.data.source.NoteFolderCrossRefDao
import com.xbot.domain.repository.FolderRepository
import com.xbot.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao,
        folderDao: FolderDao,
        noteFolderCrossRefDao: NoteFolderCrossRefDao
    ): NoteRepository {
        return NoteRepositoryImpl(noteDao, folderDao, noteFolderCrossRefDao)
    }

    @Provides
    @Singleton
    fun provideFolderRepository(
        @ApplicationContext context: Context,
        noteDao: NoteDao,
        folderDao: FolderDao
    ): FolderRepository {
        return FolderRepositoryImpl(context, noteDao, folderDao)
    }
}