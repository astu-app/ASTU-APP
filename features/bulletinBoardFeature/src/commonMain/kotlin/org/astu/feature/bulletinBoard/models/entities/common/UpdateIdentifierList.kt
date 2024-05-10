package org.astu.feature.bulletinBoard.models.entities.common

import com.benasher44.uuid.Uuid

data class UpdateIdentifierList (
    val toAdd : Set<Uuid>,
    val toRemove : Set<Uuid>,
)