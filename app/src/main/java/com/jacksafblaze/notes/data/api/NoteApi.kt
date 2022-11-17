package com.jacksafblaze.notes.data.api

import kotlinx.coroutines.delay

class NoteApi {
    suspend fun fetchNotes(){
        delay(5000)
    }
}