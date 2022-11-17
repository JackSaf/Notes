package com.jacksafblaze.notes.networkutil

import kotlinx.coroutines.flow.Flow

interface NetworkStateManager {
    fun isOnline(): Flow<Boolean>
}