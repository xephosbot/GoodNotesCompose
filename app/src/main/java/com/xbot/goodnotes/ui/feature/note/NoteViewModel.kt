package com.xbot.goodnotes.ui.feature.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xbot.domain.model.FolderModel
import com.xbot.domain.model.NoteModel
import com.xbot.domain.usecase.folder.AddFolder
import com.xbot.domain.usecase.folder.GetFolders
import com.xbot.domain.usecase.note.DeleteNotes
import com.xbot.domain.usecase.note.GetNoteCount
import com.xbot.domain.usecase.note.GetNotes
import com.xbot.domain.usecase.note.RestoreNotes
import com.xbot.domain.usecase.note.UpdateNote
import com.xbot.goodnotes.R
import com.xbot.goodnotes.mapToDomainModel
import com.xbot.goodnotes.mapToUIModel
import com.xbot.goodnotes.model.Note
import com.xbot.ui.component.Message
import com.xbot.ui.component.MessageAction
import com.xbot.ui.component.MessageContent
import com.xbot.ui.component.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val getNotes: GetNotes,
    private val getFolders: GetFolders,
    private val getNoteCount: GetNoteCount,
    private val addFolder: AddFolder,
    private val updateNote: UpdateNote,
    private val deleteNotes: DeleteNotes,
    private val restoreNotes: RestoreNotes,
    private val snackbarManager: SnackbarManager
) : ViewModel() {

    private val currentFolderId = MutableStateFlow(DEFAULT_FOLDER_ID)
    private val notes = currentFolderId.flatMapLatest { getNotes(it) }

    val state: StateFlow<NoteScreenState> = combine(
        notes, getFolders(), getNoteCount()
    ) { notesList, foldersList, noteCount ->
        NoteScreenState(
            notesList = notesList.map(NoteModel::mapToUIModel).toImmutableList(),
            foldersList = foldersList.map(FolderModel::mapToUIModel).toImmutableList(),
            noteCount = noteCount,
            currentFolderId = currentFolderId.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = NoteScreenState()
    )

    fun onAction(action: NoteScreenAction) {
        when (action) {
            is NoteScreenAction.OpenFolder -> {
                currentFolderId.update { action.folderId }
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
                val actionId = UUID.randomUUID().mostSignificantBits
                viewModelScope.launch(Dispatchers.IO) {
                    deleteNotes(
                        notes = action.notes.map(Note::mapToDomainModel),
                        folderId = state.value.currentFolderId,
                        actionId = actionId
                    )
                    snackbarManager.showMessage(
                        Message(
                            id = actionId,
                            title = MessageContent.Plurals(
                                pluralsId = R.plurals.notes_delete_snackbar,
                                quantity = action.notes.count()
                            ),
                            action = MessageAction(textId = R.string.notes_cancel_snackbar) {
                                viewModelScope.launch(Dispatchers.IO) {
                                    restoreNotes(actionId)
                                }
                            }
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_FOLDER_ID = 0L
    }
}