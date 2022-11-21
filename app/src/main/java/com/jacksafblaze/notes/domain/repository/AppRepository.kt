package com.jacksafblaze.notes.domain.repository


interface AppRepository {
    suspend fun checkFirstTimeLaunch(): Boolean //узнаем, или была первая загрузка
    suspend fun updateAppStatus()   //сохраняем данные о том, что первая загрузка была
}