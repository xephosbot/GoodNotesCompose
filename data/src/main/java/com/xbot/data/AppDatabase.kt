package com.xbot.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.note.NoteEntity
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.dao.FolderDao
import com.xbot.data.dao.NoteDao
import com.xbot.data.dao.NoteFolderCrossRefDao

@Database(
    entities = [NoteEntity::class, FolderEntity::class, NoteFolderCrossRef::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val folderDao: FolderDao
    abstract val noteFolderCrossRefDao: NoteFolderCrossRefDao

    companion object {
        const val DATABASE_NAME = "notes-db"
    }
}