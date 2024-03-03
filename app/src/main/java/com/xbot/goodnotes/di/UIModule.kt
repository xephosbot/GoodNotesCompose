package com.xbot.goodnotes.di

import com.xbot.ui.component.SnackbarManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UIModule {
    @Provides
    fun provideSnackbarManager(): SnackbarManager {
        return SnackbarManager
    }
}
