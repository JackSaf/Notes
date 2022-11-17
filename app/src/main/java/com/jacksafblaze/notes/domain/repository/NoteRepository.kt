package com.jacksafblaze.notes.domain.repository

import com.jacksafblaze.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun fetchNotesFromServer(): List<Note>

    fun getAllNotesFromDatabase(): Flow<List<Note>>

    suspend fun getNoteById(noteId: Int): Note

    suspend fun deleteNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun addAllNotes(noteList: List<Note>)

    suspend fun addNote(note: Note)


}