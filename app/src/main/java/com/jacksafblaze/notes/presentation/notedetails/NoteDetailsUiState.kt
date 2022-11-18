package com.jacksafblaze.notes.presentation.notedetails

import com.jacksafblaze.notes.domain.model.Note


data class NoteDetailsUiState(
    val note: Note? = null,
    val noteTitle: String? = null,
    val noteDescription: String? = null,
    val isSuccessfullyUpdated: Boolean = false,
    val noteIsShown: Boolean = false

)