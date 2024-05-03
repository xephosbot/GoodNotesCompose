package com.xbot.goodnotes.ui.feature.settings

data class SettingsScreenState(
    val appTheme: Int = 0,
    val useDynamicTheme: Boolean = false
)

sealed interface SettingsScreenAction {
    data class ChangeAppTheme(val appTheme: Int) : SettingsScreenAction
    data class SwitchDynamicTheme(val value: Boolean) : SettingsScreenAction
}