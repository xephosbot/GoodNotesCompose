package com.xbot.domain.model

sealed class AppTheme {
    data object Light : AppTheme()
    data object Dark : AppTheme()
    data object System : AppTheme()
}