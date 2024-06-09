package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.feature.single_window.view_models.MainRequestViewModel

class MainSingleWindowScreen : Screen {
    private lateinit var viewModel: MainRequestViewModel

    private lateinit var listOfServiceScreen: ListOfServicesSingleWindowScreen
    private val serviceScreens = mutableStateListOf<ServiceScreen>()

    @Composable
    override fun Content() {
        viewModel = remember { MainRequestViewModel() }
        listOfServiceScreen =
            ListOfServicesSingleWindowScreen(
                vm = viewModel
            )

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (maxWidth < 700.dp)
                mobileView()
            else
                desktopView()
        }
    }

    @Composable
    fun desktopView() = Row {
        Column(Modifier.weight(3f).fillMaxHeight()) {
            listOfServiceScreen.Content()
        }
        HorizontalDivider(Modifier.weight(0.01f).fillMaxHeight(), 4.dp)
        Column(Modifier.weight(6f)) {
            if (serviceScreens.any()) {
                serviceScreens.last().Content()
            }
        }
    }

    @Composable
    fun mobileView() = Column {
        if (serviceScreens.any()) {
            serviceScreens.last().Content()
        } else
            listOfServiceScreen.Content()
    }

    private fun addScreen(screen: ServiceScreen) {
        serviceScreens.add(screen)
    }
}