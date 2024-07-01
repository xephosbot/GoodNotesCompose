package com.xbot.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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

    @Query("SELECT MAX(`order`) FROM folders")
    suspend fun getMaxOrder(): Int

    @Delete
    suspend fun delete(folder: FolderEntity)

    @Delete
    suspend fun deleteAll(folders: List<FolderEntity>)

    @Query("SELECT * FROM folders")
    fun getFolders(): Flow<List<FolderEntity>>

    @Transaction
    @Query("SELECT * FROM folders WHERE folderId = :folderId")
    fun getFolderWithNotes(folderId: Long): Flow<FolderWithNotes>

    @Update(entity = FolderEntity::class)
    fun update(vararg folderUpdate: FolderUpdate)
}