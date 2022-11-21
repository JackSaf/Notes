package com.jacksafblaze.notes.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class NoteApi { //Имитация подгрузки с сервера, если я правильно понял задание
    suspend fun fetchNotes(): List<NoteNetworkEntity> = withContext(Dispatchers.IO){
        delay(5000)
        listOf()
    }
}