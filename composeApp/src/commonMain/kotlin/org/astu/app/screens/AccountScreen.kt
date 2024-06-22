package org.astu.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.app.view_models.AccountViewModel
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.common.getButtonColors
import org.astu.infrastructure.theme.CurrentColorScheme

class AccountScreen(private val onLogout: () -> Unit) : SerializableScreen {
    private lateinit var vm: AccountViewModel

    @Composable
    override fun Content() {
        vm = rememberScreenModel { AccountViewModel(onLogout) }

        val state = remember { vm.state }
        when (state.value) {
            is AccountViewModel.State.Error -> TODO()
            AccountViewModel.State.Init -> TODO()
            AccountViewModel.State.Loading -> Loading(Modifier)
            AccountViewModel.State.Show -> Show()
        }

    }

    @Composable
    fun Loading(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Show() {
        val account = vm.account

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Аккаунт") },
                    actions = {
                        // Кнопка разлогиниться
                        IconButton(onClick = vm::logout) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Logout,
                                contentDescription = "Выйти из аккаунта",
                                tint = CurrentColorScheme.primary
                            )
                        }
                    }
                )
            },
        ) {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding())
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Добрый день,\n${account.value?.fullName}!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(vertical = 24.dp),
                    )
                    HorizontalDivider()

                    if (account.value != null && account.value!!.isAdmin) {
                        Button(
                            onClick = vm::uploadFile,
                            colors = Color.getButtonColors(
                                containerColor = CurrentColorScheme.tertiaryContainer
                            ),
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.wrapContentWidth()
                            ) {
                                Icon(Icons.Outlined.PersonAdd, contentDescription = "Зарегистрировать пользователей")
                                Text("Зарегистрировать пользователей")
                            }

                        }
                        HorizontalDivider()
                    }

                    if (vm.showErrorDialog.value) {
                        ActionFailedDialog(
                            label = vm.unexpectedErrorTitle,
                            body = vm.error.value ?: vm.unexpectedErrorBody,
                            tryAgainButtonLabel = "Ок",
                            onTryAgainRequest = { vm.showErrorDialog.value = false },
                            showDismissButton = false
                        )
                    }
                }
            }
        }
    }
}
