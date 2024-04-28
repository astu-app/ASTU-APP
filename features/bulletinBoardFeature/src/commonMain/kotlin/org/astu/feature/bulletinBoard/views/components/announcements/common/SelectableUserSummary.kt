package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

class SelectableUserSummary(
    userSummary: UserSummary,
    val isSelected: MutableState<Boolean> = mutableStateOf(false),
) : UserSummary(userSummary.id, userSummary.firstName, userSummary.secondName, userSummary.patronymic)