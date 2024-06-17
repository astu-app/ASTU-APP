package org.astu.feature.universal_request.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import com.benasher44.uuid.uuid4
import org.astu.feature.universal_request.view_models.AddTemplateViewModel
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

            AddTemplateViewModel.State.Done -> TopBar {
                done(Modifier.padding(it))
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
    fun done(modifier: Modifier) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Шаблон был создан.")
            Button(onReturn) {
                Text("Вернуться")
            }
        }
    }

    @Composable
    fun Show(modifier: Modifier) {
        val (name, onChangeName) = remember { vm.name }
        val (desc, onChangeDesc) = remember { vm.description }
        Column(
            modifier.fillMaxSize(),
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Text("Название заявления")
                Spacer(Modifier.height(5.dp))
                TextField(value = name, onValueChange = onChangeName, Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                Text("Описание заявления")
                Spacer(Modifier.height(5.dp))
                TextField(value = desc, onValueChange = onChangeDesc, Modifier.fillMaxWidth())
            }
            vm.error.value?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    textAlign = TextAlign.Center,
                )
            }
            Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp), onClick = {
                vm.uploadFile()
            }) {
                Text("Выбрать файл и отправить")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(content: @Composable (PaddingValues) -> Unit) {
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
                title = { Text("Добавление шаблона", textAlign = TextAlign.Center) },
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