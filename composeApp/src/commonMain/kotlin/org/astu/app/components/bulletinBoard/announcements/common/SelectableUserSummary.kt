package org.astu.app.components.bulletinBoard.announcements.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.astu.app.components.bulletinBoard.common.models.UserSummary

class SelectableUserSummary(
    userSummary: UserSummary,
    val isSelected: MutableState<Boolean> = mutableStateOf(false),
) : UserSummary(userSummary.id, userSummary.firstName, userSummary.secondName, userSummary.patronymic)