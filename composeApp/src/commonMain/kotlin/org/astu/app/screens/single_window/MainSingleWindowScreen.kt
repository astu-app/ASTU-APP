package org.astu.app.screens.single_window

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen

data class Item(val name: String, val description: String, val props: List<Prop> = listOf())

data class Prop(val name: String, val description: String, val type: String, val value: MutableState<String?> = mutableStateOf(null))

class MainSingleWindowScreen : Screen {

    private val listOfServiceScreen =
        ListOfServicesSingleWindowScreen(onAddScreen = ::addScreen, onAddFromListScreen = { serviceScreen ->
            serviceScreens.clear()
            serviceScreens.add(serviceScreen)
        })
    private val serviceScreens = mutableStateListOf<ServiceScreen>()

    @Composable
    override fun Content() = BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth < 700.dp)
            mobileView()
        else
            desktopView()
    }

    @Composable
    fun desktopView() = Row {
        Column(Modifier.weight(3f).fillMaxHeight()) {
            listOfServiceScreen.Content()
        }
        Divider(Modifier.weight(0.01f).fillMaxHeight(), 4.dp)
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