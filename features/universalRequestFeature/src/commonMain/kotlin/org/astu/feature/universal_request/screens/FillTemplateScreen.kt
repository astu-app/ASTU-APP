package org.astu.feature.universal_request.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import cafe.adriel.voyager.core.screen.Screen
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.view_models.FillTemplateViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen

class FillTemplateScreen(var templateDTO: TemplateDTO, private val onReturn: () -> Unit) : SerializableScreen {
    private lateinit var vm: FillTemplateViewModel


    @Composable
    override fun Content() {
        vm = rememberScreenModel { FillTemplateViewModel(templateDTO) }
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
        when (state) {
            FillTemplateViewModel.State.Loading -> TopBarOfChat {
                Loading(Modifier.padding(it))
            }

            is FillTemplateViewModel.State.Error -> TODO()
            FillTemplateViewModel.State.Show -> TopBarOfChat {
                Show(Modifier.padding(it))
            }

            FillTemplateViewModel.State.Show -> TODO()
            else -> TODO()
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
    fun Show(modifier: Modifier) {
        val fields = remember { vm.templateFields }

        val context = LocalPlatformContext.current
        val pickerLauncher = rememberFilePickerLauncher(
            type = FilePickerFileType.Folder,
            selectionMode = FilePickerSelectionMode.Single,
            onResult = { files ->
                files.forEach {
//                    vm.outputPath.value = it.getName(context) ?: ""
                    println(it.getName(context))
                }
            }
        )
        Box(
            modifier.fillMaxSize(),
        ) {
            Column {
                Text(vm.name)
                Text(vm.description)
                val path = remember{vm.outputPath}
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(path.value, modifier.weight(2.0F))
//                    Button(modifier = modifier.weight(1.0f), onClick = {
//                        pickerLauncher.launch()
//                    }) {
//                        Text("Выбрать файл")
//                    }
//                }
                LazyColumn {
                    items(fields.value) {
                        TemplateListItem(it)
                    }
                }
                vm.error.value?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                Button({
                    vm.fillTemplate(vm.outputPath.value)
                }) {
                    Text("Оформить")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBarOfChat(content: @Composable (PaddingValues) -> Unit) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                { Text("Чаты", textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
//                    IconButton(vm::openCreateChatDialog) {
//                        Icon(
//                            Icons.Default.Add,
//                            contentDescription = null
//                        )
//                    }
                }
            )
        }, content = content)
    }

    @Composable
    fun TemplateListItem(template: TemplateFieldDTO) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { /*viewModel.selectChat(template)*/ },
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
                TextField(
                    value = template.value,
                    onValueChange = { textValue ->
                        vm.updateField(
                            template.copy(
                                name = template.name,
                                value = textValue
                            )
                        )
                    },
                    singleLine = true
                )
            }
        }
    }
}