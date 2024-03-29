package com.xbot.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.xbot.data.model.note.NoteEntity
import com.xbot.data.model.note.NoteUpdate
import com.xbot.data.model.note.NoteWithFolders
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    suspend fun getNote(noteId: Long): NoteEntity?

    @Delete
    suspend fun delete(note: NoteEntity)

    @Delete
    suspend fun deleteAll(notes: List<NoteEntity>)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM notes WHERE noteId = :noteId")
    fun getNoteWithFolders(noteId: Int): Flow<NoteWithFolders>

    @Update(entity = NoteEntity::class)
    suspend fun update(noteUpdate: NoteUpdate)
}