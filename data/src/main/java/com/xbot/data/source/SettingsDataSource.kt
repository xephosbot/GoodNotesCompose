package com.xbot.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val appTheme: Flow<Int> = dataStore.data.map { settings ->
        settings[APP_THEME] ?: 0
    }

    val useDynamicTheme: Flow<Boolean> = dataStore.data.map { settings ->
        settings[USE_DYNAMIC_THEME] != false
    }

    suspend fun changeAppTheme(theme: Int) {
        dataStore.edit { settings ->
            settings[APP_THEME] = theme
        }
    }

    suspend fun changeUseDynamicTheme(useDynamicTheme: Boolean) {
        dataStore.edit { settings ->
            settings[USE_DYNAMIC_THEME] = useDynamicTheme
        }
    }

    companion object {
        private val APP_THEME = intPreferencesKey("app_theme")
        private val USE_DYNAMIC_THEME = booleanPreferencesKey("use_dynamic_theme")
    }
}
