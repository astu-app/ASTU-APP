package org.astu.app.models.bulletInBoard.entities.audience

import com.benasher44.uuid.Uuid

data class UserGroup(override val id: Uuid, val name: String, val nodes: List<IAudienceNode>) : IAudienceNode