package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.repository.NoteRepository

class FetchNotesUseCase(private val repository: NoteRepository) {
    suspend fun execute(){
        repository.fetchNotesFromServer()
    }
}