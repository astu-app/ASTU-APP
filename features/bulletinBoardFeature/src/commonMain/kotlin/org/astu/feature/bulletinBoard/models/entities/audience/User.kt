package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class User(
    val id: Uuid,
    val firstName: String,
    val secondName: String,
    val patronymic: String?
)