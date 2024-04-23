package com.xbot.goodnotes.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.usecase.note.GetNote
import com.xbot.domain.usecase.note.UpdateNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNote: GetNote,
    private val updateNote: UpdateNote
) : ViewModel() {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    private val _state = MutableStateFlow(NoteDetailScreenState())
    val state: StateFlow<NoteDetailScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val note = getNote(noteId)
            if (note != null) {
                _state.update {
                    it.copy(
                        noteId = note.id,
                        noteTitle = note.title,
                        noteText = note.content,
                        noteColorId = note.colorId,
                        noteIsFavorite = note.isFavorite
                    )
                }
            }
        }
    }

    fun onAction(action: NoteDetailScreenAction) {
        when (action) {
            is NoteDetailScreenAction.UpdateNote -> {
                _state.update { it.copy(noteIsFavorite = action.isFavorite) }
                viewModelScope.launch(Dispatchers.IO) {
                    updateNote(state.value.noteId, action.isFavorite)
                }
            }

            is NoteDetailScreenAction.Save -> {

            }
        }
    }
}