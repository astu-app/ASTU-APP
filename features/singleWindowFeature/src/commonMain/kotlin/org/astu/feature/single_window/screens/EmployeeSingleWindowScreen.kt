package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.astu.feature.single_window.view_models.EmployeeRequestViewModel
import org.astu.infrastructure.SerializableScreen

class EmployeeSingleWindowScreen(private val onReturn: () -> Unit) : SerializableScreen {
    private lateinit var vm: EmployeeRequestViewModel

    @Composable
    override fun Content() {
        vm = remember { EmployeeRequestViewModel() }

        val state = remember { vm.state }
        when (state.value) {
//            MainRequestViewModel.State.Done -> TODO()
//            is MainRequestViewModel.State.Error -> TODO()
//            MainRequestViewModel.State.Init -> TODO()
            EmployeeRequestViewModel.State.Loading -> TopBar {
                Loading(Modifier.padding(it))
            }

            EmployeeRequestViewModel.State.ShowList -> TopBarOfList {
                Box(Modifier.padding(it)) {
                    vm.currentScreen.value?.Content()
                }
            }

            EmployeeRequestViewModel.State.ShowCreate -> TopBarOfCreate {
                Box(Modifier.padding(it)) {
                    vm.createScreen.value?.Content()
                }
            }

            EmployeeRequestViewModel.State.ShowScreen -> TopBar {
                Box(Modifier.padding(it)) {
                    vm.currentScreen.value?.Content()
                }
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(content: @Composable (PaddingValues) -> Unit) {
        val currentScreen = remember { vm.currentScreen }
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                navigationIcon = {
                    currentScreen.value?.onReturn?.let {
                        IconButton(it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                title = {
                    currentScreen.value?.name?.let {
                        Text(it, textAlign = TextAlign.Center)
                    } ?: Text("АГТУ.Заявка", textAlign = TextAlign.Center)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }, content = content)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBarOfScreen(content: @Composable (PaddingValues) -> Unit) {
        val currentScreen = remember { vm.currentScreen }
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                navigationIcon = {
                    currentScreen.value?.onReturn?.let {
                        IconButton(it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                title = {
                    currentScreen.value?.name?.let {
                        Text(it, textAlign = TextAlign.Center)
                    } ?: Text("АГТУ.Заявка", textAlign = TextAlign.Center)
                },
                actions = {
                    IconButton(vm::openConstructor) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }, content = content)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBarOfCreate(content: @Composable (PaddingValues) -> Unit) {
        val createScreen = remember { vm.createScreen }

        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                navigationIcon = {
                    createScreen.value?.onReturn?.let {
                        IconButton(it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                },
                title = {
                    Text("Конструктор", textAlign = TextAlign.Center)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }, content = content)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBarOfList(content: @Composable (PaddingValues) -> Unit) {
        val currentScreen = remember { vm.currentScreen }
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
                title = {
                    currentScreen.value?.name?.let {
                        Text(it, textAlign = TextAlign.Center)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    Row{
                        IconButton(vm::openConstructor) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        IconButton(vm::loadRequests) {
                            Icon(
                                Icons.Default.Update,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }, content = content)
    }

}