package com.xbot.goodnotes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember

@Composable
fun rememberGoodNotesAppState(
): GoodNotesAppState {
    return remember() {
        GoodNotesAppState()
    }
}

@Stable
class GoodNotesAppState()
