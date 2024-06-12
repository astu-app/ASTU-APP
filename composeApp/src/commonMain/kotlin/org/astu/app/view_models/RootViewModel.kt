package org.astu.app.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class RootViewModel : ScreenModel, JavaSerializable {
    fun hasAccess(): Boolean = GlobalDIContext.get<IAccountSecurityManager>().hasAccess()
}