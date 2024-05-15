package org.astu.feature.bulletinBoard.views.entities.users

import org.astu.feature.bulletinBoard.models.entities.audience.User
import kotlin.jvm.JvmName

object UserToPresentationMappers {
    @JvmName("UserCollectionToPresentations")
    fun Collection<User>.toPresentations(): List<UserSummary> =
        this.map { it.toPresentation() }

    @JvmName("UserToPresentation")
    fun User.toPresentation(): UserSummary =
        UserSummary(this.id, this.firstName, this.secondName, this.patronymic)
}