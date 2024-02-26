package org.astu.app.screens.single_window

import cafe.adriel.voyager.core.screen.Screen

abstract class ServiceScreen(val onAddScreen: (ServiceScreen) -> Unit): Screen