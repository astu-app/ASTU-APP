package org.astu.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.view_models.AccountViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen

class AccountScreen(private val onLogout: () -> Unit) : SerializableScreen {
    private lateinit var viewModel: AccountViewModel

    @Composable
    override fun Content() {
        viewModel = remember { AccountViewModel(onLogout) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement  = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(viewModel::logout) {
                Text("Разлогиниться")
            }
        }
    }
}