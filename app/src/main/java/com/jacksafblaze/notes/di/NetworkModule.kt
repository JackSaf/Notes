package com.jacksafblaze.notes.di

import com.jacksafblaze.notes.data.network.NoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideApi(): NoteApi{
        return NoteApi()
    }
}