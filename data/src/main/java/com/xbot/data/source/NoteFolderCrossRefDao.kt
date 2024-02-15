package com.xbot.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.xbot.data.model.NoteFolderCrossRef

@Dao
interface NoteFolderCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: NoteFolderCrossRef)

    @Delete
    suspend fun delete(crossRef: NoteFolderCrossRef)
}