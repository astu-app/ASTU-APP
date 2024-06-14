package org.astu.feature.single_window.screens

import org.astu.infrastructure.SerializableScreen

abstract class ServiceScreen(val name: String, val onReturn: (() -> Unit)?, val onChange: (ServiceScreen)->Unit) :
    SerializableScreen