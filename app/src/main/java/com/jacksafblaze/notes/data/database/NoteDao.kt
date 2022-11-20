package com.jacksafblaze.notes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("Select * from notes")
    fun getAllNotes(): Flow<List<NoteDbDto>>

    @Query("Select * from notes where notes.id = :id")
    suspend fun getNoteById(id: Int): NoteDbDto

    @Update
    suspend fun updateNote(noteDbDto: NoteDbDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteDbDto: NoteDbDto)

    @Delete
    suspend fun deleteNote(noteDbDto: NoteDbDto)
}