package com.xbot.data.di

import android.content.Context
import androidx.room.Room
import com.xbot.data.AppDatabase
import com.xbot.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = when (BuildConfig.DEBUG) {
        true -> Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .createFromAsset("database/goodnotes_debug.db")
            .addMigrations(AppDatabase.MIGRATION_3_4)
            .build()
        else -> Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addMigrations(AppDatabase.MIGRATION_3_4)
            .build()
    }
}