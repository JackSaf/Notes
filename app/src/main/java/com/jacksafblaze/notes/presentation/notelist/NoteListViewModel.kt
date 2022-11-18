package com.jacksafblaze.notes.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.usecases.AddNoteUseCase
import com.jacksafblaze.notes.domain.usecases.DeleteNoteUseCase
import com.jacksafblaze.notes.domain.usecases.FetchNotesUseCase
import com.jacksafblaze.notes.domain.usecases.ViewNotesUseCase
import com.jacksafblaze.notes.util.NetworkStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val viewNotesUseCase: ViewNotesUseCase,
    private val fetchNotesUseCase: FetchNotesUseCase,
    networkStateManager: NetworkStateManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState = _uiState.asStateFlow()
    private val networkState = networkStateManager.isOnline()
    var fetchJob: Job? = null
    init {
        checkNetworkConnectivity()
        viewNotes()
    }
    private fun checkNetworkConnectivity() = viewModelScope.launch {
        networkState.collect { isOnline ->
            if (isOnline) {
                fetchJob = fetchNotes()
                networkMessageShown()
            } else {
                fetchJob?.let { job ->
                    if(job.isActive){
                        job.cancel(CancellationException())
                        _uiState.update {
                            it.copy(shouldRetryRequest = true, isLoading = false)
                        }
                    }
                }
                _uiState.update {
                    it.copy(networkMessage = "Нет интернета")
                }
            }
        }
    }

    private fun viewNotes() = viewModelScope.launch {
        viewNotesUseCase.execute().collect{ noteList ->
            if(noteList.isEmpty()){
                _uiState.update { state ->
                    state.copy(dataMessage = "Нет данных")
                }
            }
            else{
                _uiState.update { state ->
                    state.copy(dataMessage = null)
                }
            }
            _uiState.update { state ->
                state.copy(noteList = noteList)
            }
        }
    }

    fun deleteNote(note: Note) = viewModelScope.launch{
        deleteNoteUseCase.execute(note)
    }

    fun addDefaultNote() = viewModelScope.launch{
        val date = LocalDateTime.now()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm")
        val dateString = date.format(dateTimeFormatter)
        val id = 0
        val title = "Новая заметка"
        val description = ""
        val isMadeToday = true
        val note = Note(id, title, description, dateString, isMadeToday)
        addNoteUseCase.execute(note)
    }

    private fun fetchNotes() = viewModelScope.launch {
        if(_uiState.value.shouldRetryRequest){
            _uiState.update {
                it.copy(shouldRetryRequest = false, isLoading = true)
            }
            fetchNotesUseCase.execute()
            _uiState.update {
                it.copy(isLaunchedForTheFirstTime = false, isLoading = false)
            }
        }
    }

    fun networkMessageShown(){
        _uiState.update {
            it.copy(networkMessage = null)
        }
    }

}