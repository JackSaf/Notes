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

    fun setTitle(title: String) {
        _uiState.update { state ->
            state.copy(noteTitle = title)
        }
    }

    fun setDescription(description: String) {
        _uiState.update { state ->
            state.copy(noteDescription = description)
        }
    }

    fun setNote(noteId: Int) =
        viewModelScope.launch {      //после того, как получаем айди, находим заметку с этим айди в бд
            try {
                val note = getNoteByIdUseCase.execute(noteId)
                _uiState.update { state ->
                    state.copy(
                        note = note,
                        noteTitle = note.title,
                        noteDescription = note.description
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }

    fun noteShown() {
        _uiState.update { state ->
            state.copy(noteIsShown = true)
        }
    }

    fun updateCurrentNote() = viewModelScope.launch {
        val noteTitle = _uiState.value.noteTitle!!
        val noteDescription = _uiState.value.noteDescription!!
        val currentNote = _uiState.value.note!!
        if (noteTitle != currentNote.title || noteDescription != currentNote.description) {   //проверяем или теперешние значения совпадают с выбранной запиской
            val date = LocalDateTime.now()                                                  //если нет, то обновляем ее, в том числе изменяя дату на теперешнюю
            val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val dateString = date.format(dateTimeFormatter)
            val updatedNote = currentNote.copy(
                title = noteTitle,
                description = noteDescription,
                lastChangesDate = dateString
            )
            try {
                updateNoteUseCase.execute(updatedNote)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
        _uiState.update {
            it.copy(isUpdatedOrLeft = true)
        }
    }
}