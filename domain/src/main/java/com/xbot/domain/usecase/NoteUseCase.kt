package com.xbot.domain.usecase

import com.xbot.domain.usecase.note.AddNote
import com.xbot.domain.usecase.note.DeleteNotes
import com.xbot.domain.usecase.note.GetNote
import com.xbot.domain.usecase.note.GetNotes
import com.xbot.domain.usecase.note.RestoreNotes
import com.xbot.domain.usecase.note.UpdateNote

data class NoteUseCase(
    val addNote: AddNote,
    val deleteNotes: DeleteNotes,
    val getNote: GetNote,
    val getNotes: GetNotes,
    val restoreNotes: RestoreNotes,
    val updateNote: UpdateNote
)
