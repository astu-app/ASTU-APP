package org.astu.feature.bulletinBoard.views.components.userGroups.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.infrastructure.components.SwitchRow

@Composable
fun ManageUserRightsDialogContent(
    canViewAnnouncements: MutableState<Boolean>,
    canCreateAnnouncements: MutableState<Boolean>,
    canCreateSurveys: MutableState<Boolean>,
    canViewUserGroupDetails: MutableState<Boolean>,
    canCreateUserGroups: MutableState<Boolean>,
    canEditUserGroups: MutableState<Boolean>,
    canEditMembers: MutableState<Boolean>,
    canEditMemberRights: MutableState<Boolean>,
    canEditUserGroupAdmin: MutableState<Boolean>,
    canDeleteUserGroup: MutableState<Boolean>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SwitchRow(
            title = "Просмотр объявлений",
            state = canViewAnnouncements
        )
        SwitchRow(
            title = "Создание объявлений",
            state = canCreateAnnouncements
        )
        SwitchRow(
            title = "Создание опросов",
            state = canCreateSurveys
        )
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SwitchRow(
            title = "Просмотр деталей групп пользователей",
            state = canViewUserGroupDetails
        )
        SwitchRow(
            title = "Создание групп пользователей",
            state = canCreateUserGroups
        )
        SwitchRow(
            title = "Редактирование групп пользователей",
            state = canEditUserGroups
        )
        SwitchRow(
            title = "Изменение состава участников группы пользователей",
            state = canEditMembers
        )
        SwitchRow(
            title = "Изменение прав участников группы пользователей",
            state = canEditMemberRights
        )
        SwitchRow(
            title = "Изменение администратора группы пользователей",
            state = canEditUserGroupAdmin
        )
        SwitchRow(
            title = "Удаление групп пользователей",
            state = canDeleteUserGroup
        )
    }
}