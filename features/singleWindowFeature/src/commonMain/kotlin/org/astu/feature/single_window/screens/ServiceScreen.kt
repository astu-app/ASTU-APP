package org.astu.feature.single_window.screens

import cafe.adriel.voyager.core.screen.Screen
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen

abstract class ServiceScreen(val onAddScreen: (ServiceScreen) -> Unit): SerializableScreen