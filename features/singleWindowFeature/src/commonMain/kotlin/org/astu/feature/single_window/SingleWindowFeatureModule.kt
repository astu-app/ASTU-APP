package org.astu.feature.single_window

import org.astu.feature.single_window.impl.SingleWindowRepositoryImpl
import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object SingleWindowFeatureModule: FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
        bind<SingleWindowRepository>() with singleton { SingleWindowRepositoryImpl() }
    })
}