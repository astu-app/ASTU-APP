package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.UserGroupDetailsViewModel
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toStaticView
import org.astu.feature.bulletinBoard.views.components.userGroups.UserGroupViewMappers.toClickableView
import org.astu.feature.bulletinBoard.views.entities.userGroups.details.UserGroupDetailsContent
import org.astu.feature.bulletinBoard.views.entities.userGroups.summary.UserGroupSummaryContent
import org.astu.feature.bulletinBoard.views.entities.users.UserToViewMappers.toView
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.components.extendedIcons.material.UserAttributes
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun UserGroupDetails(
    viewModel: UserGroupDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val detailsSnapshot = viewModel.content.value ?: return

    val navigator = LocalNavigator.currentOrThrow

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Администратор
        val adminView = remember {
            detailsSnapshot.admin?.toStaticView() ?: { Text("Администратор не задан") }
        }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Администратор", style = MaterialTheme.typography.titleMedium)
                adminView.invoke()
            }
        }

        // Участники
        val memberViews = remember { constructMemberViews(detailsSnapshot, viewModel) }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val dropdownExpanded = remember { mutableStateOf(false) }
                DropDown(memberViews, isExpanded = dropdownExpanded) { Text("Участники") }
            }
        }

        if (viewModel.showShowUserRightsDialog.value) {
            ManageUserRightsDialog(
                confirmButtonLabel = "Закрыть",
                onConfirmRequest = {
                    viewModel.showShowUserRightsDialog.value = false
                }
            ) {
                ShowUserRightsDialogContent(viewModel)
            }
        }

        // Родительские группы пользователей
        val parentViews = remember { detailsSnapshot.parents.toViews(navigator, viewModel.rootUserGroupId) }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val dropdownExpanded = remember { mutableStateOf(false) }
                DropDown(parentViews, isExpanded = dropdownExpanded) { Text("Родительские группы") }
            }
        }

        // Дочерние группы пользователей
        val childrenViews = remember { detailsSnapshot.children.toViews(navigator, viewModel.rootUserGroupId) }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val dropdownExpanded = remember { mutableStateOf(false) }
                DropDown(childrenViews, isExpanded = dropdownExpanded) { Text("Дочерние группы") }
            }
        }
    }
}


@Composable
private fun ShowUserRightsDialogContent(viewModel: UserGroupDetailsViewModel) {
    val selectedUserSnapshot = viewModel.selectedUserForRightsShowing
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(1) {
            UserRightRow("Создание объявлений", selectedUserSnapshot.value?.canCreateAnnouncements)
            UserRightRow("Создание опросов", selectedUserSnapshot.value?.canCreateSurveys)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            UserRightRow("Управление иерархией групп пользователей", selectedUserSnapshot.value?.canRuleUserGroupHierarchy)
            UserRightRow("Просмотр деталей групп пользователей", selectedUserSnapshot.value?.canViewUserGroupDetails)
            UserRightRow("Создание групп пользователей", selectedUserSnapshot.value?.canCreateUserGroups)
            UserRightRow("Редактирование групп пользователей", selectedUserSnapshot.value?.canEditUserGroups)
            UserRightRow("Изменение состава участников группы пользователей", selectedUserSnapshot.value?.canEditMembers)
            UserRightRow("Изменение прав участников группы пользователей", selectedUserSnapshot.value?.canEditMemberRights)
            UserRightRow("Изменение администратора группы пользователей", selectedUserSnapshot.value?.canEditUserGroupAdmin)
            UserRightRow("Удаление групп пользователей", selectedUserSnapshot.value?.canDeleteUserGroup)
        }
    }
}

@Composable
private fun UserRightRow(
    rightName: String,
    isAvailable: Boolean?,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
) =
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(rightName, modifier = Modifier.weight(1f))
        if (isAvailable == true) {
            Icon(Icons.Outlined.Check, null, modifier = Modifier.weight(0.1f))
        }
    }

private fun Collection<UserGroupSummaryContent>.toViews(navigator: Navigator, rootUserGroupId: Uuid): List<@Composable () -> Unit> =
    this.map {
        it.toClickableView {
            val userGroupDetailsScreen = UserGroupDetailsScreen(it.id, it.name, rootUserGroupId) { navigator.pop() }
            navigator.push(userGroupDetailsScreen)
        }
    }

private fun constructMemberViews(
    details: UserGroupDetailsContent,
    viewModel: UserGroupDetailsViewModel
): List<@Composable () -> Unit> {
    return details.members.values.map {
        val view = it.user.toView()
        return@map {
            Row() {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    view.invoke()
                }
                IconButton(
                    onClick = {
                        viewModel.selectedUserForRightsShowing.value = details.members[it.user.id]
                        viewModel.showShowUserRightsDialog.value = true
                    },
                    modifier = Modifier.weight(0.1f),
                ) {
                    Icon(Icons.Outlined.UserAttributes, "Открыть окно просмотра прав пользователей")
                }
            }
        }
    }
}
