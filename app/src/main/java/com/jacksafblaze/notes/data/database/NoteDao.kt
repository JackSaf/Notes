package com.jacksafblaze.notes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("Select * from notes")
    fun getAllNotes(): Flow<List<NoteDbDto>>

    @Query("Select * from notes where notes.isMadeToday = 1")
    suspend fun getNotesThatMadeToday(): List<NoteDbDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotes(list: List<NoteDbDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteDbDto: NoteDbDto)

    @Delete
    suspend fun deleteNote(noteDbDto: NoteDbDto)
}