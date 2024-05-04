package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

class SelectableUser(id: Uuid, firstName: String, secondName: String, patronymic: String?, val isSelected: Boolean) :
    User(id, firstName, secondName, patronymic)