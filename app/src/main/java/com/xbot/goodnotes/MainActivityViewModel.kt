package com.xbot.goodnotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.AppTheme
import com.xbot.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = combine(
        settingsRepository.appTheme,
        settingsRepository.useDynamicTheme
    ) { appTheme, useDynamicTheme ->
        MainActivityUiState.Success(appTheme, useDynamicTheme)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5000L)
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val appTheme: AppTheme,
        val useDynamicTheme: Boolean
    ) : MainActivityUiState
}
