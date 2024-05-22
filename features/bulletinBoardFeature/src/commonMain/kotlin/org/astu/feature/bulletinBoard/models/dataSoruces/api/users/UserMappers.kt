package org.astu.feature.bulletinBoard.models.dataSoruces.api.users

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.CheckableUserSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto
import org.astu.feature.bulletinBoard.models.entities.audience.CheckableUser
import org.astu.feature.bulletinBoard.models.entities.audience.User
import kotlin.jvm.JvmName

object UserMappers {
    @JvmName("UserSummaryDtoToModel")
    fun UserSummaryDto.toModel(): User =
        User(uuidFrom(this.id), this.firstName, this.secondName, this.patronymic)

    @JvmName("UserSummaryDtoCollectionToModels")
    fun Collection<UserSummaryDto>.toModels(): List<User> =
        this.map { it.toModel() }

    @JvmName("SelectableUserSummaryDtoToModel")
    fun CheckableUserSummaryDto.toModel(): CheckableUser =
        CheckableUser(uuidFrom(this.id), this.firstName, this.secondName, this.patronymic, this.isChecked)

    @JvmName("SelectableUserSummaryDtoCollectionToModels")
    fun Collection<CheckableUserSummaryDto>.toModels(): List<CheckableUser> =
        this.map { it.toModel() }
}