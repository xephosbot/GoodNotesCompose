package com.xbot.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xbot.data.model.NoteFolderCrossRef

@Dao
interface NoteFolderCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: NoteFolderCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(crossRefs: List<NoteFolderCrossRef>)

    @Query("SELECT * FROM note_folder WHERE noteId = :noteId")
    suspend fun getCrossRefsForNote(noteId: Long): List<NoteFolderCrossRef>

    @Delete
    suspend fun delete(crossRef: NoteFolderCrossRef)

    @Delete
    suspend fun deleteAll(crossRefs: List<NoteFolderCrossRef>)
}