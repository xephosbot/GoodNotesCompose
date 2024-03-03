package com.xbot.goodnotes.ui.feature.note

import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.domain.usecase.FolderUseCase
import com.xbot.domain.usecase.NoteUseCase
import com.xbot.goodnotes.R
import com.xbot.goodnotes.mapToDomainModel
import com.xbot.goodnotes.mapToUIModel
import com.xbot.goodnotes.model.Note
import com.xbot.goodnotes.ui.viewmodel.StatefulViewModel
import com.xbot.ui.component.Message
import com.xbot.ui.component.MessageAction
import com.xbot.ui.component.MessageContent
import com.xbot.ui.component.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val noteUseCase: NoteUseCase,
    private val folderUseCase: FolderUseCase
) : StatefulViewModel<NoteScreenState, NoteScreenAction>(NoteScreenState()) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCase.getNotes().onEach { notesList ->
                updateState {
                    copy(
                        notesList = notesList.map(NoteModel::mapToUIModel).toImmutableList()
                    )
                }
            }.launchIn(this)

            folderUseCase.getFolders().onEach { foldersList ->
                updateState {
                    copy(
                        foldersList = foldersList.map(FolderModel::mapToUIModel).toImmutableList()
                    )
                }
            }.launchIn(this)
        }
    }

    override fun onAction(action: NoteScreenAction) {
        when (action) {
            is NoteScreenAction.OpenFolder -> {
                updateState { copy(currentFolderId = action.folderId) }
                viewModelScope.launch(Dispatchers.IO) {
                    folderUseCase.openFolder(state.value.currentFolderId)
                }
            }

            is NoteScreenAction.AddFolder -> {
                val folder = FolderModel(name = action.name, noteCount = 0)
                viewModelScope.launch(Dispatchers.IO) {
                    folderUseCase.addFolder(folder)
                }
            }

            is NoteScreenAction.UpdateNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCase.updateNote(action.note.id, action.isFavorite)
                }
            }

            is NoteScreenAction.DeleteNotes -> {
                val actionId = UUID.randomUUID().mostSignificantBits
                viewModelScope.launch(Dispatchers.IO) {
                    noteUseCase.deleteNotes(
                        action.notes.map(Note::mapToDomainModel),
                        state.value.currentFolderId,
                        actionId
                    )
                    snackbarManager.showMessage(
                        Message(
                            id = actionId,
                            title = MessageContent.Plurals(
                                R.plurals.notes_delete_snackbar,
                                action.notes.count()
                            ),
                            action = MessageAction(textId = R.string.notes_cancel_snackbar) {
                                viewModelScope.launch(Dispatchers.IO) {
                                    noteUseCase.restoreNotes(actionId)
                                }
                            }
                        )
                    )
                }
            }
        }
    }
}