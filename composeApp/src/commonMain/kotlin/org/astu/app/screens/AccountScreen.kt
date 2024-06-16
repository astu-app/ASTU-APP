package org.astu.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.app.view_models.AccountViewModel
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.components.card.Title

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

    @Composable
    fun Show() {
        val account = vm.account
        Column {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    text = "Привет, ${account.value?.firstName}!"
                )
                HorizontalDivider()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn {
                    item {
                        ListItem(Modifier, "Выйти из аккаунта", vm::logout)
                    }
                }
            }
        }
    }

    @Composable
    fun ListItem(modifier: Modifier, title: String, onClick: () -> Unit) {
        Card(
            modifier.padding(horizontal = 10.dp, vertical = 10.dp).clickable(onClick = onClick),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.padding(start = 10.dp)) {
                    Title(title)
                }
            }
        }
    }
}
