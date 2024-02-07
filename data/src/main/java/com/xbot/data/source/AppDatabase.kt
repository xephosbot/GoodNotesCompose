package com.xbot.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xbot.data.model.FolderEntity
import com.xbot.data.model.NoteEntity
import com.xbot.data.model.NoteFolderCrossRef

@Database(
    entities = [NoteEntity::class, FolderEntity::class, NoteFolderCrossRef::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes-db"
    }
}