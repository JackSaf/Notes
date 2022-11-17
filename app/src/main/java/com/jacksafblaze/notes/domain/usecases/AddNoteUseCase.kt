package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.repository.NoteRepository

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend fun execute(note: Note){
        repository.addNote(note)
    }
}