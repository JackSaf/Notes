package com.jacksafblaze.notes.data

import com.jacksafblaze.notes.data.database.NoteDao
import com.jacksafblaze.notes.data.database.NoteDatabase
import com.jacksafblaze.notes.data.network.NoteApi
import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val database: NoteDatabase, private val api: NoteApi): NoteRepository {
    private val noteDao = database.noteDao()
    override suspend fun fetchNotesFromServer(): List<Note> {
        TODO("Not yet implemented")
    }

    override fun getAllNotesFromDatabase(): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteById(noteId: Int): Note {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun addAllNotes(noteList: List<Note>) {
        TODO("Not yet implemented")
    }

    override suspend fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

}