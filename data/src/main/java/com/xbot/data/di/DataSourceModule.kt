package com.xbot.data.di

import android.content.Context
import androidx.room.Room
import com.xbot.data.BuildConfig
import com.xbot.data.source.AppDatabase
import com.xbot.data.source.NoteDao
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
    fun provideNoteDao2(appDatabase: AppDatabase): NoteDao {
        return appDatabase.noteDao
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