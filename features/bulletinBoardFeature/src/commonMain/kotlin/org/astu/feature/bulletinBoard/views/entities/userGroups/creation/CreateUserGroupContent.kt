@file:Suppress("JoinDeclarationAndAssignment")

package org.astu.feature.bulletinBoard.views.entities.userGroups.creation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.ContentForUserGroupCreation
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toClickableView
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toSelectableView
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toStaticView
import org.astu.feature.bulletinBoard.views.components.userGroups.UserGroupViewMappers.toSelectableView
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsPresentationMapper.toPresentations
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentations

class CreateUserGroupContent(content: ContentForUserGroupCreation?) {
    val name: MutableState<String> = mutableStateOf("")

//    val adminId: MutableState<Uuid> = mutableStateOf(uuidFrom("00000000-0000-0000-0000-000000000000"))
    var admin: MutableState<UserSummary?> = mutableStateOf(null)

    val addedMembers: MutableMap<Uuid, AddUserGroupMember> = mutableMapOf()

    val selectedParentUserGroupIds: SnapshotStateList<Uuid> = mutableStateListOf()
    val selectedChildUserGroupIds: SnapshotStateList<Uuid> = mutableStateListOf()

    val selectedUserForRightsManaging: MutableState<AddUserGroupMember?> = mutableStateOf(null)
    val showManageUserRightsDialog: MutableState<Boolean> = mutableStateOf(false)

    val adminCandidates: Map<Uuid, @Composable () -> Unit>
    val memberCandidates: List<@Composable () -> Unit>
    val childUserGroupPresentations: List<@Composable () -> Unit>
    val parentUserGroupPresentations: List<@Composable () -> Unit>



    init {
        adminCandidates = content?.users
            ?.toPresentations()
            ?.associateBy(
                keySelector = { it.id },
                valueTransform = {
                    val user = it
                    user.toClickableView { admin.value = user }
                }
            ) ?: emptyMap()

        memberCandidates = content?.users
            ?.toPresentations()
            ?.map {
                val isChecked = mutableStateOf(false)
                val selectableView = it.toSelectableView(isChecked/*, Modifier.weight(1f)*/) { newState ->
                    isChecked.value = newState
                    if (newState) {
                        addedMembers[it.id] = AddUserGroupMember(it.id)
                    } else {
                        addedMembers.remove(it.id)
                    }
                }

                return@map {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        selectableView()

                        Box(modifier = Modifier
                            .requiredSize(width = 32.dp, height = 32.dp)
                            .weight(0.1f)
                        ) {
                            if (!isChecked.value)
                                return@Box

                            IconButton(
                                onClick = {
                                    selectedUserForRightsManaging.value = addedMembers[it.id]
                                    showManageUserRightsDialog.value = true
                                },
                            ) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Редактировать права пользователя")
                            }

                        }
                    }
                }
            } ?: emptyList()

        childUserGroupPresentations = content?.userGroups
            ?.toPresentations()
            ?.map {
                val isSelected = mutableStateOf(false)
                it.toSelectableView(isSelected) { newState ->
                    isSelected.value = newState
                    if (newState) {
                        selectedChildUserGroupIds.add(it.id)
                    } else {
                        selectedChildUserGroupIds.remove(it.id)
                    }
                }
            } ?: emptyList()

        parentUserGroupPresentations = content?.userGroups
            ?.toPresentations()
            ?.map {
                val isSelected = mutableStateOf(false)
                it.toSelectableView(isSelected) { newState ->
                    isSelected.value = newState
                    if (newState) {
                        selectedParentUserGroupIds.add(it.id)
                    } else {
                        selectedParentUserGroupIds.remove(it.id)
                    }
                }
            } ?: emptyList()
    }



    fun makeAdminPresentation(modifier: Modifier = Modifier): @Composable () -> Unit {
        val adminSnapshot = admin.value ?: return {
            Text("Повторно выберите администратора")
        }

        return adminSnapshot.toStaticView(modifier)
    }
}