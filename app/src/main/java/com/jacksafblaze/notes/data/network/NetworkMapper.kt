package com.jacksafblaze.notes.data.network

import com.jacksafblaze.notes.domain.model.Note
import com.jacksafblaze.notes.util.EntityMapper

object NetworkMapper : EntityMapper<NoteNetworkEntity, Note> {
    override fun mapEntityToDomainModel(entity: NoteNetworkEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            lastChangesDate = entity.lastChangesDate,
            isMadeToday = entity.isMadeToday
        )
    }

    override fun mapDomainModelToEntity(domainModel: Note): NoteNetworkEntity {
        return NoteNetworkEntity(
            id = domainModel.id,
            title = domainModel.title,
            description = domainModel.description,
            lastChangesDate = domainModel.lastChangesDate,
            isMadeToday = domainModel.isMadeToday
        )
    }

    override fun mapEntityListToDomainModelList(entityList: List<NoteNetworkEntity>): List<Note> {
        return entityList.map { mapEntityToDomainModel(it) }
    }

    override fun mapDomainModelListToEntityList(domainModelList: List<Note>): List<NoteNetworkEntity> {
        return domainModelList.map { mapDomainModelToEntity(it) }
    }
}