package com.jacksafblaze.notes.presentation

import com.jacksafblaze.notes.domain.model.Note

data class NoteListUiState(
    val noteList: List<Note>? = null,
    val isLaunchedForTheFirstTime: Boolean = false,
    val isLoading: Boolean = false,
    val shouldRetryRequest: Boolean = true,
    val networkMessage: String? = null,
    val dataMessage: String? = null
)