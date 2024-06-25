@file:Suppress("JoinDeclarationAndAssignment")

package org.astu.feature.bulletinBoard.views.entities.userGroups.editing

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.ContentForUserGroupEditing
import org.astu.feature.bulletinBoard.models.entities.audience.MemberRights
import org.astu.feature.bulletinBoard.models.entities.audience.MemberWithRights
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toClickableView
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toSelectableView
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toStaticView
import org.astu.feature.bulletinBoard.views.entities.userGroups.common.AddUserGroupMember
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentation
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentations

class EditUserGroupContent(
    private val content: ContentForUserGroupEditing
) {
    val name: MutableState<String> = mutableStateOf(content.name)
    val nameChanged: Boolean
        get() = name.value != content.name

    var admin: MutableState<UserSummary?>
    val adminChanged: Boolean
        get() = admin.value?.id != content.admin?.id


    private val checkedMemberRights: MutableMap<Uuid, AddUserGroupMember>
    val addedMembers: MutableMap<Uuid, AddUserGroupMember> = mutableMapOf()
    val memberIdsToRemove: MutableSet<Uuid> = mutableSetOf()

    val membersWithChangedRights: Collection<AddUserGroupMember>
        get() = findInitialMembersWithChangedRights()

//    val selectedParentUserGroupIds: SnapshotStateList<Uuid> = mutableStateListOf() // remove
//    val selectedChildUserGroupIds: SnapshotStateList<Uuid> = mutableStateListOf()
//
//    val addedChildUserGroupIds: MutableSet<Uuid> = mutableSetOf()
//    val deletedChildUserGroupIds: MutableSet<Uuid> = mutableSetOf()
//    val addedParentUserGroupIds: MutableSet<Uuid> = mutableSetOf()
//    val deletedParentUserGroupIds: MutableSet<Uuid> = mutableSetOf()

    val selectedUserForRightsManaging: MutableState<AddUserGroupMember?> = mutableStateOf(null)
    val showManageUserRightsDialog: MutableState<Boolean> = mutableStateOf(false)

    val adminCandidates: Map<Uuid, @Composable () -> Unit>
    val allUsers: List<@Composable () -> Unit>
//    val childUserGroupPresentations: List<@Composable () -> Unit> = listOf() // remove
//    val parentUserGroupPresentations: List<@Composable () -> Unit> = listOf()


    init {
        admin = mutableStateOf(content.admin?.toPresentation())

        checkedMemberRights = content.members
            .associateBy ({ it.user.id }, { it.toAddUserGroupMember() })
            .toMutableMap()

        adminCandidates = content.potentialMembers
            .sortedBy { it.fullName }
            .toPresentations()
            .associateBy(
                keySelector = { it.id },
                valueTransform = {
                    val user = it
                    user.toClickableView { admin.value = user }
                }
            )

        allUsers = content.potentialMembers
            .plus(content.members.map{ it.user })
            .sortedBy { it.fullName }
            .toPresentations()
            .map {
                // является ли пользователь участником группы на момент редактирования
                val userIsAlreadyMember = content.members.any { member -> member.user.id == it.id }
                val isChecked = mutableStateOf(userIsAlreadyMember)

                val selectableView = it.toSelectableView(isChecked) { newState ->
                    isChecked.value = newState

                    if (userIsAlreadyMember && !newState) {
                        // если пользователь является участником, и с него снимают выделение,
                        // его id добавляется в список "на удаление" и права удаляются из массива с правами выбранных
                        // пользователей
                        memberIdsToRemove.add(it.id)
                        checkedMemberRights.remove(it.id)

                    } else if (userIsAlreadyMember && newState) {
                        // если пользователь является участником, он не выделен, и его повторно выделяют,
                        // его id удаляется из списка "на удаление", а права возвращаются в массив с правами выбранных
                        // пользователей
                        memberIdsToRemove.remove(it.id)
                        checkedMemberRights[it.id] = content.members
                            .firstOrNull { m -> m.user.id == it.id }!!
                            .toAddUserGroupMember()

                    } else if (!userIsAlreadyMember && newState) {
                        // если пользователь не является участником, и его выделяют,
                        // он добавляется в список "на добавление", и его права добавляются в массив с правами выбранных
                        // пользователей
                        val newMember = AddUserGroupMember(it.id)
                        addedMembers[it.id] = newMember
                        checkedMemberRights[it.id] = newMember

                    } else {
                        // если пользователь не является участником, он выделен, и с него снимают выделение,
                        // он удаляется из списка "на добавление", и его права удаляются из массива с правами выбранных
                        // пользователей
                        addedMembers.remove(it.id)
                        checkedMemberRights.remove(it.id)
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
                                    selectedUserForRightsManaging.value = checkedMemberRights[it.id]
                                    showManageUserRightsDialog.value = true
                                },
                            ) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Редактировать права пользователя")
                            }

                        }
                    }
                }
            }

//        childUserGroupPresentations = content.userGroups // remove
//            ?.toPresentations()
//            ?.map {
//                val isSelected = mutableStateOf(false)
//                it.toSelectableView(isSelected) { newState ->
//                    isSelected.value = newState
//                    if (newState) {
//                        selectedChildUserGroupIds.add(it.id)
//                    } else {
//                        selectedChildUserGroupIds.remove(it.id)
//                    }
//                }
//            } ?: emptyList()
//
//        parentUserGroupPresentations = content?.userGroups
//            ?.toPresentations()
//            ?.map {
//                val isSelected = mutableStateOf(false)
//                it.toSelectableView(isSelected) { newState ->
//                    isSelected.value = newState
//                    if (newState) {
//                        selectedParentUserGroupIds.add(it.id)
//                    } else {
//                        selectedParentUserGroupIds.remove(it.id)
//                    }
//                }
//            } ?: emptyList()
    }



    fun canEdit(): Boolean {
        return name.value.isNotBlank()
    }

    fun makeAdminPresentation(modifier: Modifier = Modifier): @Composable () -> Unit {
        val adminSnapshot = admin.value ?: return {
            Text("Повторно выберите администратора")
        }

        return adminSnapshot.toStaticView(modifier)
    }



    private fun findInitialMembersWithChangedRights(): List<AddUserGroupMember> {
        // Ищем изначальных участников, с которых не было снято выделение. Такие пользователи присутствуют и в списке
        // изначальных участников, и в списке с отмеченными участниками
        val checkedInitialMemberIds = checkedMemberRights.keys.intersect(
            content.members
                .map { it.user.id }
                .toSet()
        )

        val initialMembersWithChangedRights = mutableListOf<AddUserGroupMember>()
        checkedInitialMemberIds.forEach { id ->
            val initialMemberRights = content.members.first { it.user.id == id }.rights
            val checkedMemberRights  = checkedMemberRights[id]!!

            if (!areRightsEqual(initialMemberRights, checkedMemberRights))
                initialMembersWithChangedRights.add(checkedMemberRights)
        }

        return initialMembersWithChangedRights
    }

    private fun areRightsEqual(left: MemberRights, right: AddUserGroupMember) =
        left.canCreateAnnouncements == right.canCreateAnnouncements.value &&
        left.canCreateSurveys == right.canCreateSurveys.value &&
        left.canRuleUserGroupHierarchy == right.canRuleUserGroupHierarchy.value &&
        left.canViewUserGroupDetails == right.canViewUserGroupDetails.value &&
        left.canCreateUserGroups == right.canCreateUserGroups.value &&
        left.canEditUserGroups == right.canEditUserGroups.value &&
        left.canEditMembers == right.canEditMembers.value &&
        left.canEditMemberRights == right.canEditMemberRights.value &&
        left.canEditUserGroupAdmin == right.canEditUserGroupAdmin.value &&
        left.canDeleteUserGroup == right.canDeleteUserGroup.value

    private fun MemberWithRights.toAddUserGroupMember(): AddUserGroupMember =
        AddUserGroupMember(
            userId = this.user.id,
            canCreateAnnouncements = mutableStateOf(this.rights.canCreateAnnouncements),
            canCreateSurveys = mutableStateOf(this.rights.canCreateSurveys),
            canRuleUserGroupHierarchy = mutableStateOf(this.rights.canRuleUserGroupHierarchy),
            canViewUserGroupDetails = mutableStateOf(this.rights.canViewUserGroupDetails),
            canCreateUserGroups = mutableStateOf(this.rights.canCreateUserGroups),
            canEditUserGroups = mutableStateOf(this.rights.canEditUserGroups),
            canEditMembers = mutableStateOf(this.rights.canEditMembers),
            canEditMemberRights = mutableStateOf(this.rights.canEditMemberRights),
            canEditUserGroupAdmin = mutableStateOf(this.rights.canEditUserGroupAdmin),
            canDeleteUserGroup = mutableStateOf(this.rights.canDeleteUserGroup),
        )
}