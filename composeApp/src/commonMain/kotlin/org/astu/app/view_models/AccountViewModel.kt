package org.astu.app.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.infrastructure.DependencyInjection.GlobalDIContext

class AccountViewModel(private val onLogout: () -> Unit) : ScreenModel {
    private val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()

    fun logout() {
        accountSecurityManager.logout()
        onLogout()
    }
}