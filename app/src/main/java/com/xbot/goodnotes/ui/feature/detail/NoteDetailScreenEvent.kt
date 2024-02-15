package com.xbot.goodnotes.ui.feature.detail

sealed interface NoteDetailScreenEvent {
    data object Save : NoteDetailScreenEvent
}