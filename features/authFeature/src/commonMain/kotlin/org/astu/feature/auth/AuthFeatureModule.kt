package org.astu.feature.auth

import org.astu.feature.auth.impl.AccountSecurityManager
import org.astu.feature.auth.impl.AstuAccountRepository
import org.astu.feature.auth.impl.AstuAuthRepository
import org.astu.feature.auth.impl.BearerSecurityHttpClient
import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.astu.infrastructure.IAccountRepository
import org.astu.infrastructure.SecurityHttpClient
import org.astu.infrastructure.security.IAccountSecurityManager
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object AuthFeatureModule : FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
        //Real
        bind<AuthRepository>() with singleton { AstuAuthRepository() }
//        bind<AuthServiceConfig>() with singleton { AuthServiceConfig("https://localhost:50001/") }
//        bind<AuthServiceConfig>() with singleton { AuthServiceConfig("https://192.168.3.16:50001/") }
        //Fakea
//        bind<AuthRepository>() with singleton { FakeAuthRepository() }

        bind<IAccountRepository>() with singleton { AstuAccountRepository() }

        bind<SecurityHttpClient>() with singleton { BearerSecurityHttpClient() }
        bind<IAccountSecurityManager>() with singleton { AccountSecurityManager() }
    })
}