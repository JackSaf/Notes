package com.jacksafblaze.notes.domain.usecases

import com.jacksafblaze.notes.domain.repository.AppRepository

class CheckFirstTimeLaunchUseCase(private val repository: AppRepository) {
    suspend fun execute(): Boolean {
        return repository.checkFirstTimeLaunch()
    }
}