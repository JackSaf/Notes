package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.repository.AppRepository

class UpdateAppStatusUseCase(private val repository: AppRepository) {
    suspend fun execute(){
        repository.updateAppStatus()
    }
}