package org.astu.feature.bulletinBoard.views.entities.users

import com.benasher44.uuid.Uuid

class CheckableUserSummary(
    id: Uuid,
    firstName: String,
    secondName: String,
    patronymic: String?,
    val isChecked: Boolean
) : UserSummary(id, firstName, secondName, patronymic)