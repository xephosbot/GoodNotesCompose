package com.xbot.data.di

import android.content.Context
import androidx.room.Room
import com.xbot.data.source.AppDatabase
import com.xbot.data.source.FolderDao
import com.xbot.data.source.NoteDao
import com.xbot.data.source.NoteFolderCrossRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideNoteDao(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao
    }

    @Provides
    @Singleton
    fun provideFolderDao(appDatabase: AppDatabase): FolderDao {
        return appDatabase.folderDao
    }

    @Provides
    @Singleton
    fun provideNoteFolderCrossRefDao(appDatabase: AppDatabase): NoteFolderCrossRefDao {
        return appDatabase.noteFolderCrossRefDao
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).createFromAsset("database/goodnotes_release.db").build()
    }
}