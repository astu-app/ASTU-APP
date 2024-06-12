package org.astu.app.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class AccountViewModel(private val onLogout: () -> Unit) : ScreenModel, JavaSerializable {
    fun logout() {
        val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
        accountSecurityManager.logout()
        onLogout()
    }
}