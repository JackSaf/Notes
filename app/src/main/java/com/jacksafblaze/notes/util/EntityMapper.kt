package com.jacksafblaze.notes.util

interface EntityMapper<Entity, DomainModel> {  // мапперы в таком маленьком проекте необязательны, можно и через одну модельку
    fun mapEntityToDomainModel(entity: Entity): DomainModel
    fun mapDomainModelToEntity(domainModel: DomainModel): Entity
    fun mapEntityListToDomainModelList(entityList: List<Entity>): List<DomainModel>
    fun mapDomainModelListToEntityList(domainModelList: List<DomainModel>): List<Entity>
}