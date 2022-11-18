package com.jacksafblaze.notes.presentation.notelist

import com.jacksafblaze.notes.domain.model.Note

data class NoteListUiState(
    val noteList: List<Note>? = null,
    val isLaunchedForTheFirstTime: Boolean = true,
    val isLoading: Boolean = false,
    val shouldRetryRequest: Boolean = true,
    val networkMessage: String? = null,
    val dataMessage: String? = null
)