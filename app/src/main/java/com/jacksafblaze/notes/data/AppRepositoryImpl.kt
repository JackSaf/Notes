package com.jacksafblaze.notes.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.jacksafblaze.notes.domain.repository.AppRepository
import kotlinx.coroutines.flow.first

private const val dataStoreKey = "IS_LAUNCHED_FOR_THE_FIRST_TIME"

class AppRepositoryImpl(private val dataStore: DataStore<Preferences>) : AppRepository {
    override suspend fun checkFirstTimeLaunch(): Boolean {
        val key = booleanPreferencesKey(dataStoreKey)
        val preferences = dataStore.data.first()
        return preferences[key] == null || preferences[key] == true
    }


    override suspend fun updateAppStatus() {
        val key = booleanPreferencesKey(dataStoreKey)
        dataStore.edit { preferences ->
            preferences[key] = false
        }
    }
}