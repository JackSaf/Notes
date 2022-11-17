package com.jacksafblaze.notes.presentation

import com.jacksafblaze.notes.domain.model.Note

data class NoteListUiState(
    val noteList: List<Note> = listOf(),
    val isLaunchedForTheFirstTime: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null
)