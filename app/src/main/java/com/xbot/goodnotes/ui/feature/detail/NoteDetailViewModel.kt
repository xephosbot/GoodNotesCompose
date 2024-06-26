package com.xbot.goodnotes.ui.feature.detail

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.common.Constants
import com.xbot.domain.usecase.note.AddNote
import com.xbot.domain.usecase.note.GetNote
import com.xbot.domain.usecase.note.UpdateNote
import com.xbot.goodnotes.mapToDomainModel
import com.xbot.goodnotes.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNote: GetNote,
    private val updateNote: UpdateNote,
    private val addNote: AddNote
) : ViewModel() {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    val titleTextFieldState: TextFieldState = TextFieldState()
    val contentTextFieldState: TextFieldState = TextFieldState()

    private val _state = MutableStateFlow(NoteDetailScreenState())
    val state: StateFlow<NoteDetailScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val note = getNote(noteId)
            if (note != null) {
                titleTextFieldState.edit { replace(0, length, note.title) }
                contentTextFieldState.edit { replace(0, length, note.content) }

                _state.update {
                    it.copy(
                        noteId = note.id,
                        noteTitle = note.title,
                        noteText = note.content,
                        noteTimestamp = note.timeStamp,
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
                viewModelScope.launch(Dispatchers.IO) {
                    updateNote(state.value.noteId, action.isFavorite)
                    _state.update { it.copy(noteIsFavorite = action.isFavorite) }
                }
            }

            is NoteDetailScreenAction.ChangeNoteColor -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.update { it.copy(noteColorId = action.colorId) }
                }
            }

            is NoteDetailScreenAction.Save -> {
                val changeTimeStamp = !titleTextFieldState.text.contentEquals(state.value.noteTitle)
                        || !contentTextFieldState.text.contentEquals(state.value.noteText)

                val noteId = when (state.value.noteId) {
                    Constants.NEW_NOTE_ID -> 0L
                    else -> state.value.noteId
                }

                val timestamp = when (changeTimeStamp) {
                    true -> Clock.System.now().toEpochMilliseconds() / 1000
                    else -> state.value.noteTimestamp
                }

                viewModelScope.launch(Dispatchers.IO) {
                    val note = Note(
                        id = noteId,
                        title = titleTextFieldState.text.toString(),
                        content = contentTextFieldState.text.toString(),
                        isFavorite = state.value.noteIsFavorite,
                        timeStamp = timestamp,
                        colorId = state.value.noteColorId
                    )
                    addNote(note.mapToDomainModel())
                }
            }
        }
    }
}