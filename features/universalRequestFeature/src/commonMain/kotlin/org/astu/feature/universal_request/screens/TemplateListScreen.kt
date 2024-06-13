package org.astu.feature.universal_request.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
//import com.mohamedrejeb.calf.core.LocalPlatformContext
//import com.mohamedrejeb.calf.io.getName
//import com.mohamedrejeb.calf.picker.FilePickerFileType
//import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
//import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.view_models.TemplateListViewModel
import org.astu.infrastructure.SerializableScreen

class TemplateListScreen : SerializableScreen {
    private lateinit var vm: TemplateListViewModel


    @Composable
    override fun Content() {
        vm = rememberScreenModel { TemplateListViewModel() }
//        val context = LocalPlatformContext.current
//        val pickerLauncher = rememberFilePickerLauncher(
//            type = FilePickerFileType.Word,
//            selectionMode = FilePickerSelectionMode.Single,
//            onResult = { files ->
//                files.forEach {
//                    println(it.getName(context))
//                }
//            }
//        )
        val state by remember { vm.state }
        val screen by remember { vm.screen }
        when (state) {
            is TemplateListViewModel.State.Error -> TopBar {
                ErrorScreen(Modifier.padding(it))
            }
            TemplateListViewModel.State.Init -> TODO()
            TemplateListViewModel.State.Loading -> TopBar {
                Loading(Modifier.padding(it))
            }
            TemplateListViewModel.State.ShowTemplate -> screen?.Content()
            TemplateListViewModel.State.ShowAddScreen -> screen?.Content()
            TemplateListViewModel.State.ShowList -> {
                TopBar {
                    ShowList(Modifier.padding(it))
                }
            }
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
    fun ErrorScreen(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
             val state = vm.state.value as TemplateListViewModel.State.Error
            Text(state.message)

        }
    }

    @Composable
    fun ShowList(modifier: Modifier) {
        val templates = remember { vm.templates }
        Box(
            modifier.fillMaxSize(),
        ) {
            LazyColumn {
                items(templates.value) {
                    TemplateListItem(it)
                    HorizontalDivider()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(content: @Composable (PaddingValues) -> Unit) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                { Text("Универсальные заявления", textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(vm::openAddScreen) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        }, content = content)
    }

    @Composable
    fun TemplateListItem(template: TemplateDTO) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { vm.openScreen(template) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = template.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                )

                Text(
                    text = template.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}