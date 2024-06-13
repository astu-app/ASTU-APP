package org.astu.feature.universal_request.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.view_models.FillTemplateViewModel
import org.astu.infrastructure.SerializableScreen

class FillTemplateScreen(var templateDTO: TemplateDTO, val onReturn: () -> Unit) :
    SerializableScreen {
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

        LazyColumn(
            modifier.fillMaxSize(),
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        vm.name,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(vm.description, fontSize = 24.sp)
                }
                HorizontalDivider()
            }

            items(fields.value) {
                TemplateListItem(it)
            }
            item {
                vm.error.value?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
            item {
                Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp), onClick = {
                    vm.fillTemplate()
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
                navigationIcon = {
                    IconButton(onReturn) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                title = { Text("Заполнение заявления", textAlign = TextAlign.Center) },
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
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                Text(
                    text = template.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = template.value,
                    onValueChange = { textValue ->
                        vm.updateField(
                            template.copy(
                                name = template.name,
                                value = textValue
                            )
                        )
                    },
                    singleLine = false
                )
            }
        }
    }
}