package com.xbot.goodnotes.ui

import androidx.compose.runtime.Composable
import com.xbot.goodnotes.navigation.GoodNotesNavHost
import com.xbot.goodnotes.ui.feature.note.NoteScreen

@Composable
fun GoodNotesApp(
    appState: GoodNotesAppState = rememberGoodNotesAppState()
) {
    GoodNotesNavHost(
        appState = appState
    )
}
