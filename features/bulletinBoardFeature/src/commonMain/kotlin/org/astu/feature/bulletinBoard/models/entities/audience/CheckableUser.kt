package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

class CheckableUser(id: Uuid, firstName: String, secondName: String, patronymic: String?, val isChecked: Boolean) :
    User(id, firstName, secondName, patronymic)