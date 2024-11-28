package com.xbot.data

import android.annotation.SuppressLint
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xbot.data.model.folder.FolderEntity
import com.xbot.data.model.note.NoteEntity
import com.xbot.data.model.NoteFolderCrossRef
import com.xbot.data.dao.FolderDao
import com.xbot.data.dao.NoteDao
import com.xbot.data.dao.NoteFolderCrossRefDao

@Database(
    entities = [NoteEntity::class, FolderEntity::class, NoteFolderCrossRef::class],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
    abstract fun getFolderDao(): FolderDao
    abstract fun getNoteFolderCrossRefDao(): NoteFolderCrossRefDao

    companion object {
        const val DATABASE_NAME = "notes-db"

        val MIGRATION_3_4 = object : Migration(3, 4) {
            @SuppressLint("Range")
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE folders ADD COLUMN `order` INTEGER NOT NULL DEFAULT 0")

                val cursor = db.query("SELECT folderId FROM folders ORDER BY folderId")
                var order = 0
                while (cursor.moveToNext()) {
                    val folderId = cursor.getLong(cursor.getColumnIndex("folderId"))
                    db.execSQL("UPDATE folders SET `order` = $order WHERE folderId = $folderId")
                    order++
                }
                cursor.close()
            }
        }
    }
}
