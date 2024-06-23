package org.astu.app.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.astu.app.view_models.AllServicesViewModel
import org.astu.feature.single_window.screens.EmployeeSingleWindowScreen
import org.astu.feature.single_window.screens.MainSingleWindowScreen
import org.astu.feature.single_window.screens.UserSingleWindowScreen
import org.astu.feature.single_window.view_models.EmployeeCreatedRequestViewModel
import org.astu.feature.universal_request.screens.TemplateListScreen
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.components.card.Description
import org.astu.infrastructure.components.card.Title

class AllServicesScreen : SerializableScreen {
    private lateinit var viewModel: AllServicesViewModel

    @Composable
    override fun Content() {
        viewModel = remember { AllServicesViewModel() }
        val currentScreen = remember { viewModel.currentScreen }
        key(currentScreen.value) {
            if (currentScreen.value == null)
                TopBar {
                    list(Modifier.padding(it))
                }
            else
                currentScreen.value?.Content()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(content: @Composable (PaddingValues) -> Unit) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                title = {
                    Text("Услуги", textAlign = TextAlign.Center)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }, content = content)
    }

    @Composable
    fun list(modifier: Modifier) {
        val services = remember{ viewModel.services }
        LazyColumn(modifier) {
            items(services.value) {
                ListItem(
                    modifier =  Modifier.fillMaxWidth(),
                    it.title,
                    it.description,
                    it.onClick
                )
            }
        }
    }

    @Composable
    fun ListItem(modifier: Modifier, title: String, description: String, onClick: () -> Unit) {
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
                    Description(description, fontSize = 12.sp)
                }
            }
        }
    }
}