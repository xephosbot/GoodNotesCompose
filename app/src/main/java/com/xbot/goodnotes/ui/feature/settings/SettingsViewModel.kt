package com.xbot.goodnotes.ui.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.repository.SettingsRepository
import com.xbot.goodnotes.toAppTheme
import com.xbot.goodnotes.toInt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val state: StateFlow<SettingsScreenState> = combine(
        settingsRepository.appTheme,
        settingsRepository.useDynamicTheme
    ) { appTheme, useDynamicTheme ->
        SettingsScreenState(
            appTheme = appTheme.toInt(),
            useDynamicTheme = useDynamicTheme
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = SettingsScreenState(),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onAction(action: SettingsScreenAction) {
        when (action) {
            is SettingsScreenAction.ChangeAppTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    settingsRepository.changeAppTheme(action.appTheme.toAppTheme())
                }
            }

            is SettingsScreenAction.SwitchDynamicTheme -> {
                viewModelScope.launch(Dispatchers.IO) {
                    settingsRepository.changeUseDynamicTheme(action.value)
                }
            }
        }
    }
}