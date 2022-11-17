package com.jacksafblaze.notes.util

import kotlinx.coroutines.flow.Flow

interface NetworkStateManager {
    fun isOnline(): Flow<Boolean>
}