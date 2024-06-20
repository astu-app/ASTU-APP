package org.astu.feature.single_window.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.astu.feature.single_window.entities.EmployeeCreatedRequest
import org.astu.feature.single_window.view_models.EmployeeRequestViewModel
import org.astu.infrastructure.components.card.Description
import org.astu.infrastructure.components.card.Title


class EmployeeListOfRequestSingleWindowScreen(
    val vm: EmployeeRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Список заявлений", onReturn, onChange) {
    @Composable
    override fun Content() {
        val requests = remember { vm.requests }
        val count = requests.value.any()
        if (count)
            VerticalListOfServices()
        else
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Заявлений нет")
            }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun VerticalListOfServices() {
        val requests = remember { vm.requests }

        LazyColumn {
            items(requests.value) {
                ListItem(Modifier.fillMaxWidth(), it)
                HorizontalDivider(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    thickness = 2.dp
                )
            }
        }
    }

    @Composable
    fun ListItem(modifier: Modifier, createdRequest: EmployeeCreatedRequest) {
        Card(
            modifier.padding(horizontal = 10.dp, vertical = 10.dp).clickable {
                vm.openRequestScreen(createdRequest)
            },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.padding(start = 10.dp)) {
                    Title(createdRequest.name)
                    Description(createdRequest.description, fontSize = 12.sp)
                }
            }
        }
    }
}