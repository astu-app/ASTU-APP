package org.astu.feature.bulletinBoard.views.entities.users

import com.benasher44.uuid.Uuid

open class UserSummary(val id: Uuid, val firstName: String, val secondName: String, val patronymic: String?) {
    val fullName: String
        get() =
            if (patronymic != null) "$secondName $firstName $patronymic"
            else "$secondName $firstName"
}