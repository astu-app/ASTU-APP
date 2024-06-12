package org.astu.app.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.app.notifications.NotificationManager
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.infrastructure.DependencyInjection.GlobalDIContext.inject
import org.astu.infrastructure.JavaSerializable

class AuthorizedViewModel : ScreenModel, JavaSerializable {
    init {
        screenModelScope.launch {
            NotificationManager.loadExistingNotifications()
        }
    }



    fun logout() {
        val securityManager by inject<IAccountSecurityManager>()
        securityManager.logout()
    }
}