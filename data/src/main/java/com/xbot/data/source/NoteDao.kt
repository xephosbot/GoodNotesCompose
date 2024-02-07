package com.xbot.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.xbot.data.model.FolderEntity
import com.xbot.data.model.FolderWithNoteCount
import com.xbot.data.model.FolderWithNotes
import com.xbot.data.model.NoteEntity
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.model.NoteWithFolders
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: FolderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteFolderCrossRef(crossRef: NoteFolderCrossRef)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query(
        """
        SELECT folders.*, COUNT(note_folder.noteId) AS noteCount
        FROM folders
        LEFT JOIN note_folder ON folders.folderId = note_folder.folderId
        GROUP BY folders.folderId
    """
    )
    fun getFolders(): Flow<List<FolderWithNoteCount>>

    @Transaction
    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun getFolderWithNotes(folderId: Long): Flow<FolderWithNotes>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    fun getNoteWithFolders(noteId: Long): Flow<NoteWithFolders>

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    suspend fun getNote(noteId: Long): NoteEntity?

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNoteCount(): Int

    @Delete
    suspend fun deleteFolder(folder: FolderEntity)

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Delete
    suspend fun deleteNoteFolderCrossRef(crossRef: NoteFolderCrossRef)
}