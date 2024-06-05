package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.userGroups.UserGroupHierarchyViewModel
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.CreateUserGroupScreen
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupsScreen(private val onReturn: () -> Unit) : Screen {
//    private val screens: List<UnitOfNavigationBar> = // remove
//        listOf(
//            UnitOfNavigationBar(
//                label = { Text("Иерархия") },
//                icon = { Icon(Icons.Default.Newspaper, contentDescription = null) },
//                screen = UserGroupHierarchyScreen()
//            ),
////            UnitOfNavigationBar(
////                label = { Text("Список") },
////                icon = { Icon(TablerIcons.FileExport, contentDescription = null) },
////                screen = UserGroupListScreen()
////            )
//        )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
//        val selected = remember { mutableStateOf(0) } // remove

        val viewModel = rememberScreenModel { UserGroupHierarchyViewModel(navigator) }
        val screen = UserGroupHierarchyScreen(viewModel)

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Группы пользователей") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createUserGroupScreen = CreateUserGroupScreen { navigator.pop() }
                        navigator.push(createUserGroupScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Add, "Создать группу пользователей")
                }
            },
            actions = {
                // Кнопка обновить
                Button(
                    onClick = { viewModel.loadUserGroups() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CurrentColorScheme.surface,
                        disabledContainerColor = CurrentColorScheme.surface,
                        contentColor = CurrentColorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Обновить",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
//            bottomBar = { NavigationBarScreens(selected, screens, setValue) } // remove
        ) {
//            screens.getOrNull(selected)?.screen?.Content() // remove
            screen.Content()
        }
    }




}