package com.xbot.data.di

import com.xbot.data.dao.FolderDao
import com.xbot.data.dao.NoteDao
import com.xbot.data.dao.NoteFolderCrossRefDao
import com.xbot.data.repository.FolderRepositoryImpl
import com.xbot.data.repository.NoteRepositoryImpl
import com.xbot.data.repository.SettingsRepositoryImpl
import com.xbot.data.source.SettingsDataSource
import com.xbot.domain.repository.FolderRepository
import com.xbot.domain.repository.NoteRepository
import com.xbot.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao,
        noteFolderCrossRefDao: NoteFolderCrossRefDao
    ): NoteRepository {
        return NoteRepositoryImpl(noteDao, noteFolderCrossRefDao)
    }

    @Provides
    @Singleton
    fun provideFolderRepository(
        folderDao: FolderDao
    ): FolderRepository {
        return FolderRepositoryImpl(folderDao)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        dataSource: SettingsDataSource
    ): SettingsRepository {
        return SettingsRepositoryImpl(dataSource)
    }
}