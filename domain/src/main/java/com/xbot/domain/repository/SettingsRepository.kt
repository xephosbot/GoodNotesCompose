package com.xbot.domain.repository

import com.xbot.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val appTheme: Flow<AppTheme>

    val useDynamicTheme: Flow<Boolean>

    suspend fun changeAppTheme(appTheme: AppTheme)

    suspend fun changeUseDynamicTheme(useDynamicTheme: Boolean)
}