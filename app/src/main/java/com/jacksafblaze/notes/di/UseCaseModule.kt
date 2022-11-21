package com.jacksafblaze.notes.di

import com.jacksafblaze.notes.domain.repository.AppRepository
import com.jacksafblaze.notes.domain.repository.NoteRepository
import com.jacksafblaze.notes.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCaseModule {
    @Provides
    fun provideAddNoteUseCase(repository: NoteRepository): AddNoteUseCase{
        return AddNoteUseCase(repository)
    }
    @Provides
    fun provideDeleteNoteUseCase(repository: NoteRepository): DeleteNoteUseCase{
        return DeleteNoteUseCase(repository)
    }
    @Provides
    fun provideFetchNotesUseCase(repository: NoteRepository): FetchNotesUseCase{
        return FetchNotesUseCase(repository)
    }
    @Provides
    fun provideGetNoteByIdUseCase(repository: NoteRepository): GetNoteByIdUseCase{
        return GetNoteByIdUseCase(repository)
    }
    @Provides
    fun provideUpdateNoteUseCase(repository: NoteRepository): UpdateNoteUseCase{
        return UpdateNoteUseCase(repository)
    }
    @Provides
    fun provideViewNotesUseCase(repository: NoteRepository): ViewNotesUseCase{
        return ViewNotesUseCase(repository)
    }
    @Provides
    fun provideCheckFirstTimeLaunchUseCase(repository: AppRepository): CheckFirstTimeLaunchUseCase{
        return CheckFirstTimeLaunchUseCase(repository)
    }
    @Provides
    fun provideUpdateAppStatusUseCase(repository: AppRepository): UpdateAppStatusUseCase{
        return UpdateAppStatusUseCase(repository)
    }
}