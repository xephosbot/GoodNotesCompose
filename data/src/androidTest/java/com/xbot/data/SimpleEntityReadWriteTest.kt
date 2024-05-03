package com.xbot.data

import android.content.Context
import androidx.datastore.core.IOException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.xbot.data.dao.NoteDao
import com.xbot.data.model.note.NoteEntity
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var noteDao: NoteDao
    private lateinit var db: AppDatabase

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        noteDao = db.noteDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeUserAndReadInList() = runTest {
        val notes = createNoteList(10)
        noteDao.insertAll(notes)
        noteDao.getNotes().take(1).collect {
            assertEquals(10, it.count())
        }
    }

    private fun createNoteList(count: Int) = (1..count).map { index ->
        NoteEntity(
            noteId = index.toLong(),
            title = "Test Note $index",
            content = "Sample text",
            timeStamp = 0L,
            colorId = 0
        )
    }
}