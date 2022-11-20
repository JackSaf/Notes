package com.jacksafblaze.notes.data.database

import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.util.EntityMapper

object DatabaseMapper : EntityMapper<NoteDbDto, Note> {
    override fun mapEntityToDomainModel(entity: NoteDbDto): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            lastChangesDate = entity.lastChangesDate
        )
    }

    override fun mapDomainModelToEntity(domainModel: Note): NoteDbDto {
        return NoteDbDto(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            lastChangesDate = domainModel.lastChangesDate
        )
    }

    override fun mapEntityListToDomainModelList(entityList: List<NoteDbDto>): List<Note> {
        return entityList.map { mapEntityToDomainModel(it) }
    }

    override fun mapDomainModelListToEntityList(domainModelList: List<Note>): List<NoteDbDto> {
        return domainModelList.map { mapDomainModelToEntity(it) }
    }
}