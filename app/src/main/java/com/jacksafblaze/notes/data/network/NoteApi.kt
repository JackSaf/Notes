package com.jacksafblaze.notes.data.network

import kotlinx.coroutines.delay

class NoteApi {
    suspend fun fetchNotes(): List<NoteNetworkEntity>{
        delay(5000)
        return listOf()
    }
}