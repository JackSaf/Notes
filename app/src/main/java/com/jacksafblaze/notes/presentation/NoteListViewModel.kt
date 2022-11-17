package com.jacksafblaze.notes.presentation

import androidx.lifecycle.ViewModel
import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.usecases.ViewNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteListViewModel(private val viewNotesUseCase: ViewNotesUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState = _uiState.asStateFlow()

    fun deleteNote(note: Note){

    }
}