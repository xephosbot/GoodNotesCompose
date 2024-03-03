package com.xbot.goodnotes.ui

import androidx.compose.runtime.Composable
import com.xbot.goodnotes.navigation.GoodNotesNavHost

@Composable
fun GoodNotesApp(
    appState: GoodNotesAppState = rememberGoodNotesAppState()
) {
    GoodNotesNavHost(
        appState = appState
    )
}
