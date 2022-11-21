package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class ViewNotesUseCase(private val repository: NoteRepository) {
    fun execute(): Flow<List<Note>>{
        return repository.getAllNotes()
    }
}