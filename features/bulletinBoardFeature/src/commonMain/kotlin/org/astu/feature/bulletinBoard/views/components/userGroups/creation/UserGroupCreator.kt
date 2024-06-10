package org.astu.feature.bulletinBoard.views.components.userGroups.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.CreateUserGroupViewModel
import org.astu.feature.bulletinBoard.views.components.userGroups.ManageUserRightsDialog
import org.astu.feature.bulletinBoard.views.components.userGroups.common.ManageUserRightsDialogContent
import org.astu.feature.bulletinBoard.views.components.userGroups.common.SelectAdminSection
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupCreator(
    val viewModel: CreateUserGroupViewModel
) : ContentProvider, DefaultModifierProvider {
    @Composable
    override fun Content(modifier: Modifier) {
        val createContentSnapshot = remember { viewModel.content.value!! }

        val defaultCardModifier = remember {
            Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        }
        Column(modifier = modifier) {
            // Название
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                ),
                modifier = defaultCardModifier,
            ) {
                OutlinedTextField(
                    value = createContentSnapshot.name.value,
                    onValueChange = { newText: String -> createContentSnapshot.name.value = newText },
                    label = { Text("Название") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Админ
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                ),
                modifier = defaultCardModifier,
            ) {
                SelectAdminSection(
                    admin = createContentSnapshot.admin.value,
                    adminCandidates = createContentSnapshot.adminCandidates,
                    adminPresentation = createContentSnapshot.makeAdminPresentation()
                ) {
                    viewModel.resetAdmin()
                }
            }

            // Участники
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                ),
                modifier = defaultCardModifier,
            ) {
                DropDown(createContentSnapshot.memberCandidates, modifier = Modifier.padding(16.dp)) {
                    Text("Участники")
                }
            }

            if (createContentSnapshot.showManageUserRightsDialog.value) {
                ManageUserRightsDialog(
                    onConfirmRequest = {
                        createContentSnapshot.showManageUserRightsDialog.value = false
                    }
                ) {
                    ManageUserRightsDialogContent()
                }
            }

            // Родительские группы пользователей
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                ),
                modifier = defaultCardModifier,
            ) {
                DropDown(createContentSnapshot.parentUserGroupPresentations, modifier = Modifier.padding(16.dp)) {
                    Text("Родительские группы")
                }
            }

            // Дочерние группы пользователей
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                ),
                modifier = defaultCardModifier,
            ) {
                DropDown(createContentSnapshot.childUserGroupPresentations, modifier = Modifier.padding(16.dp)) {
                    Text("Дочерние группы")
                }
            }
        }
    }

    override fun getDefaultModifier(): Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(all = 8.dp)



    @Composable
    private fun ManageUserRightsDialogContent() {
        val selectedUserSnapshot = viewModel.content.value?.selectedUserForRightsManaging?.value ?: return

        ManageUserRightsDialogContent(
            canViewAnnouncements = selectedUserSnapshot.canViewAnnouncements,
            canCreateAnnouncements = selectedUserSnapshot.canCreateAnnouncements,
            canCreateSurveys = selectedUserSnapshot.canCreateSurveys,
            canViewUserGroupDetails = selectedUserSnapshot.canViewUserGroupDetails,
            canCreateUserGroups = selectedUserSnapshot.canCreateUserGroups,
            canEditUserGroups = selectedUserSnapshot.canEditUserGroups,
            canEditMembers = selectedUserSnapshot.canEditMembers,
            canEditMemberRights = selectedUserSnapshot.canEditMemberRights,
            canEditUserGroupAdmin = selectedUserSnapshot.canEditUserGroupAdmin,
            canDeleteUserGroup = selectedUserSnapshot.canDeleteUserGroup,
        )
    }
}