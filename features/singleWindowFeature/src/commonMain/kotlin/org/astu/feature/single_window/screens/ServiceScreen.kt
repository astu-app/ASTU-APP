package org.astu.feature.single_window.screens

import cafe.adriel.voyager.core.screen.Screen

abstract class ServiceScreen(val onAddScreen: (ServiceScreen) -> Unit): Screen