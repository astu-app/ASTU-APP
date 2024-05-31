package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.CreateUserGroupViewModel
import org.astu.feature.bulletinBoard.views.components.userGroups.ManageUserRightsDialog
import org.astu.feature.bulletinBoard.views.entities.userGroups.creation.CreateUserGroupContent
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.components.SwitchRow
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.theme.CurrentColorScheme

class CreateUserGroupScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { CreateUserGroupViewModel() }

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Новая группа") },
            actions = {
                Button(
                    enabled = viewModel.canCreate(),
                    onClick = { viewModel.create() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CurrentColorScheme.surface,
                        disabledContainerColor = CurrentColorScheme.surface,
                        contentColor = CurrentColorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Создать",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when (state) {
                CreateUserGroupViewModel.State.CreateContentLoading -> Loading()
                CreateUserGroupViewModel.State.CreateContentLoadingDone -> CreateUserGroupSection(viewModel)
                CreateUserGroupViewModel.State.CreateContentLoadingError -> showErrorDialog(viewModel)
                CreateUserGroupViewModel.State.NewUserGroupUploading -> Loading()
                CreateUserGroupViewModel.State.NewUserGroupUploadingDone -> onReturn()
                CreateUserGroupViewModel.State.NewUserGroupUploadingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel,
                    body = viewModel.errorDialogBody,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
                    onDismissRequest = viewModel.onErrorDialogDismissRequest
                )
            }
        }
    }

    @Composable
    private fun CreateUserGroupSection(
        viewModel: CreateUserGroupViewModel
    ) {
        val createContentSnapshot = remember { viewModel.content.value!! }

        val defaultCardModifier = remember {
            Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        }
        Column {
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
                SelectAdminSection(createContentSnapshot, viewModel)
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
                    ManageUserRightsDialogContent(createContentSnapshot)
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


    @Composable
    private fun ManageUserRightsDialogContent(createContentSnapshot: CreateUserGroupContent) {
        val selectedUserSnapshot = createContentSnapshot.selectedUserForRightsManaging
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SwitchRow(
                "Просмотр объявлений",
                selectedUserSnapshot.value?.canViewAnnouncements ?: mutableStateOf(true)
            )
            SwitchRow(
                "Создание объявлений",
                selectedUserSnapshot.value?.canCreateAnnouncements ?: mutableStateOf(false)
            )
            SwitchRow(
                "Создание опросов",
                selectedUserSnapshot.value?.canCreateSurveys ?: mutableStateOf(false)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SwitchRow(
                "Просмотр деталей групп пользователей",
                selectedUserSnapshot.value?.canViewUserGroupDetails ?: mutableStateOf(false)
            )
            SwitchRow(
                "Создание групп пользователей",
                selectedUserSnapshot.value?.canCreateUserGroups ?: mutableStateOf(false)
            )
            SwitchRow(
                "Редактирование групп пользователей",
                selectedUserSnapshot.value?.canEditUserGroups ?: mutableStateOf(false)
            )
            SwitchRow(
                "Изменение состава участников группы пользователей",
                selectedUserSnapshot.value?.canEditMembers ?: mutableStateOf(false)
            )
            SwitchRow(
                "Изменение прав участников группы пользователей",
                selectedUserSnapshot.value?.canEditMemberRights ?: mutableStateOf(false)
            )
            SwitchRow(
                "Изменение администратора группы пользователей",
                selectedUserSnapshot.value?.canEditUserGroupAdmin ?: mutableStateOf(false)
            )
            SwitchRow(
                "Удаление групп пользователей",
                selectedUserSnapshot.value?.canDeleteUserGroup ?: mutableStateOf(false)
            )
        }
    }

    @Composable
    private fun SelectAdminSection(
        createContentSnapshot: CreateUserGroupContent,
        viewModel: CreateUserGroupViewModel
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (createContentSnapshot.admin.value == null) {
                // Если админ не выбран, показываем элемент для выбора
                DropDown(
                    createContentSnapshot.adminCandidates.values,
                ) {
                    Text("Администратор")
                }

            } else {
                // Если же админ выбран, то отображаем его краткие данные

                Text("Администратор")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    createContentSnapshot.makeAdminPresentation().invoke()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "Удалить администратора",
                            modifier = Modifier.clickable { viewModel.resetAdmin() }
                        )
                    }
                }
            }
        }
    }

    private fun showErrorDialog(viewModel: CreateUserGroupViewModel) {
        viewModel.showErrorDialog = true
    }
}