package com.xbot.data.di

import android.content.Context
import com.xbot.data.repository.NoteRepositoryImpl
import com.xbot.data.source.NoteDao
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
        @ApplicationContext context: Context,
        noteDao: NoteDao
    ): NoteRepository {
        return NoteRepositoryImpl(context, noteDao)
    }
}