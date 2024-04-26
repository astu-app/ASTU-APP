package org.astu.app.models.bulletInBoard.entities.audience

import com.benasher44.uuid.Uuid

data class UserGroup(val id: Uuid, val name: String, val userGroups: MutableList<UserGroup>, val members: List<User>)