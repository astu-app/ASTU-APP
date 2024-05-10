package org.astu.feature.bulletinBoard.models.dataSoruces.api.common.mappers

import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.dtos.UpdateIdentifierListDto
import org.astu.feature.bulletinBoard.models.entities.common.UpdateIdentifierList

object UpdateIdentifierListMappers {
    fun UpdateIdentifierList?.toDto(): UpdateIdentifierListDto =
        UpdateIdentifierListDto(
            toAdd = this?.toAdd?.map { it.toString() }?.toSet() ?: emptySet(),
            toRemove = this?.toRemove?.map { it.toString() }?.toSet() ?: emptySet(),
        )
}