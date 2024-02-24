package com.xbot.goodnotes.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.xbot.domain.usecase.GetNoteUseCase
import com.xbot.goodnotes.ui.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteUseCase: GetNoteUseCase
) : StatefulViewModel<NoteDetailScreenState, NoteDetailScreenEvent>(NoteDetailScreenState()) {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    init {
        viewModelScope.launch {
            noteUseCase(noteId)?.let { note ->
                updateState {
                    copy(
                        noteId = note.id,
                        noteTitle = note.title,
                        noteText = note.content,
                        noteColorId = note.colorId
                    )
                }
            }
        }
    }

    override fun onAction(action: NoteDetailScreenEvent) {
        when (action) {
            is NoteDetailScreenEvent.Save -> {

            }
        }
    }
}