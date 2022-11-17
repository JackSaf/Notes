package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.repository.NoteRepository

class GetNoteByIdUseCase(private val repository: NoteRepository) {
    suspend fun execute(noteId: Int): Note{
        return repository.getNoteById(noteId)
    }
}