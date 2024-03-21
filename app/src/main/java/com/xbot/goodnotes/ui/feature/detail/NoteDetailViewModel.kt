package com.xbot.goodnotes.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.xbot.domain.usecase.note.GetNote
import com.xbot.goodnotes.ui.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNote: GetNote
) : StatefulViewModel<NoteDetailScreenState, NoteDetailScreenAction>(NoteDetailScreenState()) {

    private val noteId: Long = checkNotNull(savedStateHandle["noteId"])

    init {
        viewModelScope.launch {
            getNote(noteId)?.let { note ->
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

    override fun onAction(action: NoteDetailScreenAction) {
        when (action) {
            is NoteDetailScreenAction.Save -> {

            }
        }
    }
}