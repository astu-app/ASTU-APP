package org.astu.feature.universal_request.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import org.astu.feature.universal_request.view_models.AddTemplateViewModel
import org.astu.feature.universal_request.view_models.FillTemplateViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen

class AddTemplateScreen(private val onReturn: () -> Unit) : SerializableScreen {
    private lateinit var vm: AddTemplateViewModel


    @Composable
    override fun Content() {
        vm = rememberScreenModel { AddTemplateViewModel() }
        val state by remember { vm.state }
        when (state) {
            AddTemplateViewModel.State.Loading -> TopBar {
                Loading(Modifier.padding(it))
            }

            AddTemplateViewModel.State.Show -> TopBar {
                Show(Modifier.padding(it))
            }

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
        val (name, onChangeName) = remember { vm.name }
        val (desc, onChangeDesc) = remember { vm.description }
        Box(
            modifier.fillMaxSize(),
        ) {
            Column(Modifier.padding(start = 16.dp)) {
                Text("Название заявления")
                Spacer(Modifier.height(5.dp))
                TextField(value = name, onValueChange = onChangeName, Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                Text("Описание заявления")
                Spacer(Modifier.height(5.dp))
                TextField(value = desc, onValueChange = onChangeDesc, Modifier.fillMaxWidth())
            }
            Button({
                vm.uploadFile()
            }){
                Text("Выбрать файл и отправить")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(content: @Composable (PaddingValues) -> Unit) {
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

}