package org.astu.app.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.security.IAccountSecurityManager

class RootViewModel : ScreenModel, JavaSerializable {
    fun hasAccess(): Boolean = GlobalDIContext.get<IAccountSecurityManager>().hasAccess()

    val currentScreen = mutableStateOf<SerializableScreen?>(null)

}