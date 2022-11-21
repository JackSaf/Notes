package com.jacksafblaze.notes.presentation.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.usecases.*
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
    private val checkFirstTimeLaunchUseCase: CheckFirstTimeLaunchUseCase,
    private val updateAppStatusUseCase: UpdateAppStatusUseCase,
    networkStateManager: NetworkStateManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState = _uiState.asStateFlow()
    private val networkState = networkStateManager.isOnline()
    private var fetchJob: Job? = null

    init {
        viewNotes()
        startAutoRefetch()
    }

    private fun startAutoRefetch() =
        viewModelScope.launch {
            checkFirstTimeLaunch()                                  //проверяем состояние приложения перед загрузкой
            networkState.collect { isOnline ->                       //подписываемся на состоние интернета
                if (!_uiState.value.isFetched) {                     //если данные не подгружены
                    fetchJob?.cancel()                               //отменяем загрузку, если таковая была
                    if (!isOnline) {                                //если интернета нет
                        _uiState.update { state ->                  //показываем сообщение об этом
                            state.copy(
                                isLoading = false,
                                noNetworkMessage = "Нет интернета"
                            )
                        }
                    } else {
                        fetchJob = fetchNotes()
                        _uiState.update { state ->
                            state.copy(noNetworkMessage = null)
                        }
                    }
                }
            }
        }

    private fun viewNotes() = viewModelScope.launch {
        try {
            viewNotesUseCase.execute().collect { noteList ->
                if (noteList.isEmpty()) {                           //если получаемый список пуст
                    _uiState.update { state ->                      //показываем сообщение об этом
                        state.copy(noDataMessage = "Нет данных")
                    }
                } else {
                    _uiState.update { state ->              //иначе убираем его
                        state.copy(noDataMessage = null, noteList = noteList)
                    }
                }
            }
        }
        catch (e: Exception){
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    private fun fetchNotes() = viewModelScope.launch {
        _uiState.update { state ->
            state.copy(isLoading = true)
        }
        try {
            fetchNotesUseCase.execute()
            _uiState.update {
                it.copy(isLoading = false, isFetched = true)
            }                                           //тогда, и только тогда, когда первая загрузка получилась, обновляем статус приложения
            updateFirstTimeLaunch()                     //если юзер зайдет, а потом быстро закроет приложение - не считается :)
        }
        catch (e: Exception){
            if(e !is CancellationException){            //показывать сообщение об отмене корутины нам не надо
                _uiState.update {
                    it.copy(errorMessage = e.message)
                }
            }
        }
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        try {
            deleteNoteUseCase.execute(note)
        }
        catch(e: Exception){
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    fun addDefaultNote() = viewModelScope.launch {      //создаем дефолтную записку
        val date = LocalDateTime.now()
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateString = date.format(dateTimeFormatter)
        val id = 0
        val title = "Новая заметка"
        val description = ""
        val note = Note(id, title, description, dateString)
        try {
            addNoteUseCase.execute(note)
        }
        catch(e: Exception){
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    private suspend fun checkFirstTimeLaunch() {
        try {
            val firstTimeLaunch = checkFirstTimeLaunchUseCase.execute()
            _uiState.update {
                it.copy(firstTimeLaunch = firstTimeLaunch)
            }
        }
        catch(e: Exception){
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    private fun updateFirstTimeLaunch() = viewModelScope.launch {
        _uiState.update {
            it.copy(firstTimeLaunch = false)
        }
        try {
            updateAppStatusUseCase.execute()
        }
        catch(e: Exception){
            _uiState.update {
                it.copy(errorMessage = e.message)
            }
        }
    }

    fun networkMessageShown() {
        _uiState.update {
            it.copy(noNetworkMessage = null)
        }
    }

    fun errorMessageShown(){
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

}