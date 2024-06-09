package org.astu.app

import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule

expect object AppModule : FeatureModule {
    override fun init(): DependencyInjector
}