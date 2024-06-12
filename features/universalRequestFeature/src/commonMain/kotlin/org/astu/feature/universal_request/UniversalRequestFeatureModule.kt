package org.astu.feature.universal_request

import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object UniversalRequestFeatureModule: FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
        bind<UniversalTemplateRepository>() with singleton { UniversalTemplateRepositoryImpl() }
    })
}