package com.xbot.goodnotes.ui.feature.note

import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.domain.usecase.AddFolderUseCase
import com.xbot.domain.usecase.DeleteNotesUseCase
import com.xbot.domain.usecase.GetFoldersUseCase
import com.xbot.domain.usecase.GetNotesUseCase
import com.xbot.domain.usecase.OpenFolderUseCase
import com.xbot.domain.usecase.RestoreNotesUseCase
import com.xbot.domain.usecase.UpdateNoteUseCase
import com.xbot.goodnotes.mapToDomainModel
import com.xbot.goodnotes.mapToUIModel
import com.xbot.goodnotes.model.Note
import com.xbot.goodnotes.ui.viewmodel.StatefulViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val folders: GetFoldersUseCase,
    private val notes: GetNotesUseCase,
    private val addFolder: AddFolderUseCase,
    private val openFolder: OpenFolderUseCase,
    private val deleteNotes: DeleteNotesUseCase,
    private val restoreNotes: RestoreNotesUseCase,
    private val updateNote: UpdateNoteUseCase
) : StatefulViewModel<NoteScreenState, NoteScreenAction>(NoteScreenState()) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            notes().onEach { notesList ->
                updateState { copy(notesList = notesList.map(NoteModel::mapToUIModel).toImmutableList()) }
            }.launchIn(this)

            folders().onEach { foldersList ->
                updateState { copy(foldersList = foldersList.map(FolderModel::mapToUIModel).toImmutableList()) }
            }.launchIn(this)
        }
    }

    override fun onAction(action: NoteScreenAction) {
        when (action) {
            is NoteScreenAction.OpenFolder -> {
                updateState { copy(currentFolderId = action.folderId) }
                viewModelScope.launch(Dispatchers.IO) {
                    openFolder(state.value.currentFolderId)
                }
            }
            is NoteScreenAction.AddFolder -> {
                val folder = FolderModel(name = action.name, noteCount = 0)
                viewModelScope.launch(Dispatchers.IO) {
                    addFolder(folder)
                }
            }
            is NoteScreenAction.UpdateNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateNote(action.note.id, action.isFavorite)
                }
            }
            is NoteScreenAction.DeleteNotes -> {
                viewModelScope.launch(Dispatchers.IO) {
                    deleteNotes(action.notes.map(Note::mapToDomainModel))
                }
            }
            is NoteScreenAction.UndoDelete -> {
                viewModelScope.launch(Dispatchers.IO) {
                    restoreNotes()
                }
            }
        }
    }
}