package org.astu.feature.bulletinBoard.views.entities.userGroups.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid

data class AddUserGroupMember (
    val userId: Uuid,

    val canViewAnnouncements: MutableState<Boolean> = mutableStateOf(true),
    val canCreateAnnouncements: MutableState<Boolean> = mutableStateOf(false),
    val canCreateSurveys: MutableState<Boolean> = mutableStateOf(false),
    val canRuleUserGroupHierarchy: MutableState<Boolean> = mutableStateOf(false),
    val canViewUserGroupDetails: MutableState<Boolean> = mutableStateOf(false),
    val canCreateUserGroups: MutableState<Boolean> = mutableStateOf(false),
    val canEditUserGroups: MutableState<Boolean> = mutableStateOf(false),
    val canEditMembers: MutableState<Boolean> = mutableStateOf(false),
    val canEditMemberRights: MutableState<Boolean> = mutableStateOf(false),
    val canEditUserGroupAdmin: MutableState<Boolean> = mutableStateOf(false),
    val canDeleteUserGroup: MutableState<Boolean> = mutableStateOf(false),
)