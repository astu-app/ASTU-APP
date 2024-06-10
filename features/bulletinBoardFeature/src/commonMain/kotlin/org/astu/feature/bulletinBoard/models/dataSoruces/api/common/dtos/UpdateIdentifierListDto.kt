package org.astu.feature.bulletinBoard.models.dataSoruces.api.common.dtos

import kotlinx.serialization.Serializable

/**
 * Объект для обновления списка прикрепленных идентификаторов. Null, если список идентификаторов не требуется изменять
 */
@Serializable
data class UpdateIdentifierListDto(
    var toAdd: Set<String>,
    var toRemove: Set<String>,
)
