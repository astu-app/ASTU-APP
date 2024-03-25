package org.astu.app

import org.astu.infrastructure.DependencyInjector
import org.astu.infrastructure.FeatureModule

expect object AppModule: FeatureModule {
    override fun init(): DependencyInjector
}