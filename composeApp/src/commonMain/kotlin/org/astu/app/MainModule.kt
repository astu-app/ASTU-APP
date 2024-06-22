package org.astu.app

import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.NotificationServerConfig
import org.astu.infrastructure.registration.RegistrationService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object MainModule: FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
        bind<GatewayServiceConfig>() with singleton { GatewayServiceConfig("http://api2.ttraum.ru/") }
        bind<NotificationServerConfig>() with singleton { NotificationServerConfig(
            host = "notify2.ttraum.ru",
            clientId = 1,
            clientToken = "CjO4CU7tmHjaaug" // todo изменить токен приложения
        ) }

        bind<AccountUser>() with singleton { AccountUser() }
        bind<RegistrationService>() with singleton { RegistrationService() }
    })
}