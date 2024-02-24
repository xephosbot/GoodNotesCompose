package com.xbot.goodnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class StatefulViewModel<S, A>(initialValue: S) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)
    val state: StateFlow<S> = _state.asStateFlow()

    abstract fun onAction(action: A)

    protected fun updateState(transform: S.() -> S) {
        _state.update { it.transform() }
    }
}