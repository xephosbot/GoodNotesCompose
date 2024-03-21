package com.xbot.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.xbot.data.source.SettingsDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideSettingsDataSource(
        dataStore: DataStore<Preferences>
    ): SettingsDataSource {
        return SettingsDataSource(dataStore)
    }
}