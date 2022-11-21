package com.jacksafblaze.notes.presentation.notelist

import com.jacksafblaze.notes.domain.model.Note

data class NoteListUiState(
    val noteList: List<Note>? = null,
    val firstTimeLaunch: Boolean = false,
    val isLoading: Boolean = false,
    val isFetched: Boolean = false,
    val noNetworkMessage: String? = null,
    val noDataMessage: String? = null,
    val errorMessage: String? = null
)