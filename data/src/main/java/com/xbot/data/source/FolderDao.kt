package com.xbot.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.folder.FolderUpdate
import com.xbot.data.model.folder.FolderWithNotes
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: FolderEntity): Long

    @Query("SELECT * FROM folders WHERE position = :position")
    suspend fun getFolder(position: Int): FolderEntity?

    @Delete
    suspend fun delete(folder: FolderEntity)

    @Transaction
    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun getFolderWithNotes(folderId: Long): Flow<FolderWithNotes>

    @Query(
        """
        SELECT folders.*, COUNT(note_folder.noteId) as noteCount
        FROM folders LEFT JOIN note_folder ON folders.folderId = note_folder.folderId
        GROUP BY folders.folderId
        ORDER BY folders.position ASC
        """
    )
    fun getFoldersWithNoteCount(): Flow<Map<FolderEntity, @MapColumn(columnName = "noteCount") Int>>

    @Update(entity = FolderEntity::class)
    suspend fun update(folderUpdate: FolderUpdate)

    @Transaction
    suspend fun updates(updates: List<FolderUpdate>) {
        updates.forEach { update(it) }
    }
}