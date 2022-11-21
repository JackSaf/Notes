package com.jacksafblaze.notes.util

import kotlinx.coroutines.flow.Flow

interface NetworkStateManager { //Для проверки состояния соединения
    fun isOnline(): Flow<Boolean>
}