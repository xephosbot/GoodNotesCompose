package com.xbot.data.repository

import com.xbot.data.source.SettingsDataSource
import com.xbot.data.toAppTheme
import com.xbot.data.toInt
import com.xbot.domain.model.AppTheme
import com.xbot.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: SettingsDataSource
): SettingsRepository {

    override val appTheme: Flow<AppTheme> = dataSource.appTheme.map(Int::toAppTheme)

    override val useDynamicTheme: Flow<Boolean> = dataSource.useDynamicTheme

    override suspend fun changeAppTheme(appTheme: AppTheme) {
        dataSource.changeAppTheme(appTheme.toInt())
    }

    override suspend fun changeUseDynamicTheme(useDynamicTheme: Boolean) {
        dataSource.changeUseDynamicTheme(useDynamicTheme)
    }
}