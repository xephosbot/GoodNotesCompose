package com.xbot.ui.component

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Stable
data class Message(
    val id: Long,
    val title: MessageContent,
    val action: MessageAction? = null
)

data class MessageAction(
    @StringRes val textId: Int,
    val action: () -> Unit = {}
)

sealed class MessageContent {
    data class Text(@StringRes val textId: Int) : MessageContent()
    data class Plurals(@PluralsRes val pluralsId: Int, val quantity: Int) : MessageContent()
}

object SnackbarManager {

    private val _messages: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<Message>> get() = _messages.asStateFlow()

    fun showMessage(message: Message) {
        _messages.update { currentMessages ->
            currentMessages + message
        }
    }

    fun setMessageShown(messageId: Long) {
        _messages.update { currentMessages ->
            currentMessages.filterNot { it.id == messageId }
        }
    }
}
