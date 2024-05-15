package org.astu.feature.bulletinBoard.models.dataSoruces.api.users

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.SelectableUserSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto
import org.astu.feature.bulletinBoard.models.entities.audience.SelectableUser
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
    fun SelectableUserSummaryDto.toModel(): SelectableUser =
        SelectableUser(uuidFrom(this.id), this.firstName, this.secondName, this.patronymic, this.isSelected)

    @JvmName("SelectableUserSummaryDtoCollectionToModels")
    fun Collection<SelectableUserSummaryDto>.toModels(): List<SelectableUser> =
        this.map { it.toModel() }
}