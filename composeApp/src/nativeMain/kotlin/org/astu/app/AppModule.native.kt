package org.astu.app

import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule

actual object AppModule : FeatureModule {
    actual override fun init(): DependencyInjector {
        TODO("Not yet implemented")
    }
}