package com.xbot.goodnotes.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class StatefulViewModel<S, E>(initialValue: S) : ViewModel() {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)
    val state: StateFlow<S> = _state.asStateFlow()

    abstract fun onEvent(event: E)

    protected fun updateState(transform: S.() -> S) {
        _state.update { it.transform() }
    }

    protected fun log(message: String) {
        Log.d(this::class.simpleName, message)
    }
}