package org.astu.app.models.bulletInBoard.entities.audience

import com.benasher44.uuid.Uuid

data class User(
    override val id: Uuid,
    val firstName: String,
    val secondName: String,
    val patronymic: String?
) : IAudienceNode