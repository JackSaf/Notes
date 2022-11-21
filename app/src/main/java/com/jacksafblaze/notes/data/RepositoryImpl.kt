package com.jacksafblaze.notes.data

import com.jacksafblaze.notes.data.database.DatabaseMapper
import com.jacksafblaze.notes.data.database.NoteDatabase
import com.jacksafblaze.notes.data.network.NetworkMapper
import com.jacksafblaze.notes.data.network.NoteApi
import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RepositoryImpl(database: NoteDatabase, private val api: NoteApi): NoteRepository {
    private val noteDao = database.noteDao()
    override suspend fun fetchNotesFromServer() {                                        //Загружаем, а после сохраняем в бд
        val entityList = api.fetchNotes()                                               //Можно было сделать с помощью NetworkBoundResource
        val domainModelList = NetworkMapper.mapEntityListToDomainModelList(entityList) //но тогда было бы сложнее с автоматическим ретраем после реконекта
        addAllNotes(domainModelList)
    }

    private suspend fun addAllNotes(list: List<Note>){
        val dbDtoList = DatabaseMapper.mapDomainModelListToEntityList(list)
        noteDao.insertAllNotes(dbDtoList)
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { DatabaseMapper.mapEntityListToDomainModelList(it) }
    }

    override suspend fun getNoteById(noteId: Int): Note {
        val noteDbDto = noteDao.getNoteById(noteId)
        return DatabaseMapper.mapEntityToDomainModel(noteDbDto)
    }

    override suspend fun deleteNote(note: Note) {
        val noteDbDto = DatabaseMapper.mapDomainModelToEntity(note)
        noteDao.deleteNote(noteDbDto)
    }

    override suspend fun updateNote(note: Note) {
        val noteDbDto = DatabaseMapper.mapDomainModelToEntity(note)
        noteDao.updateNote(noteDbDto)
    }


    override suspend fun addNote(note: Note) {
        val noteDbDto = DatabaseMapper.mapDomainModelToEntity(note)
        noteDao.insertNote(noteDbDto)
    }

}