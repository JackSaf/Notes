package com.jacksafblaze.notes.presentation.notedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.notes.domain.usecases.GetNoteByIdUseCase
import com.jacksafblaze.notes.domain.usecases.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteDetailsUiState())
    val uiState = _uiState.asStateFlow()

    fun setTitle(title: String){
        _uiState.update { state ->
            state.copy(noteTitle = title)
        }
    }

    fun setDescription(description: String){
        _uiState.update { state ->
            state.copy(noteDescription = description)
        }
    }

    fun setNote(noteId: Int) = viewModelScope.launch {
        val note = getNoteByIdUseCase.execute(noteId)
        _uiState.update {state ->
            state.copy(note = note, noteTitle = note.title, noteDescription = note.description)
        }
    }
    fun updateCurrentNote() = viewModelScope.launch {
        val noteTitle = _uiState.value.noteTitle!!
        val noteDescription = _uiState.value.noteDescription!!
        val currentNote = _uiState.value.note!!
        if(noteTitle != currentNote.title || noteDescription != currentNote.description){
            val date = LocalDateTime.now()
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")
            val dateString = date.format(dateTimeFormatter)
            val updatedNote = currentNote.copy(title = noteTitle, description = noteDescription, lastChangesDate = dateString)
            updateNoteUseCase.execute(updatedNote)
            _uiState.update { state ->
                state.copy(isSuccessfullyUpdated = true)
            }
        }
    }
}