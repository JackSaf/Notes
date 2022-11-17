package com.jacksafblaze.notes.di

import com.jacksafblaze.notes.data.RepositoryImpl
import com.jacksafblaze.notes.data.database.NoteDatabase
import com.jacksafblaze.notes.data.network.NoteApi
import com.jacksafblaze.notes.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(database: NoteDatabase, api: NoteApi, ): NoteRepository{
        return RepositoryImpl(database, api)
    }
}