package org.astu.feature.bulletinBoard.models.entities.audience

data class MemberWithRights(
    val user: User,
    val rights: MemberRights
)
