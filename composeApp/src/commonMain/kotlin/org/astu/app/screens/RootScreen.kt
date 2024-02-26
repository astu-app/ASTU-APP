package org.astu.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class RootScreen : Screen {
    private val hasAccess =  mutableStateOf(false)

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        nav.popAll()
        if (/*Unauthorized*/hasAccess.value) {
            nav.push(AuthorizedScreen())
        } else {
            nav.push(UnauthorizedScreen{
                hasAccess.value = true
                nav.pop()
            })
        }
    }
}