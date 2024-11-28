package com.xbot.data.di

import com.xbot.data.AppDatabase
import com.xbot.data.dao.FolderDao
import com.xbot.data.dao.NoteDao
import com.xbot.data.dao.NoteFolderCrossRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    @Singleton
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.getNoteDao()
    }

    @Provides
    @Singleton
    fun provideFolderDao(appDatabase: AppDatabase): FolderDao {
        return appDatabase.getFolderDao()
    }

    @Provides
    @Singleton
    fun provideNoteFolderCrossRefDao(appDatabase: AppDatabase): NoteFolderCrossRefDao {
        return appDatabase.getNoteFolderCrossRefDao()
    }
}