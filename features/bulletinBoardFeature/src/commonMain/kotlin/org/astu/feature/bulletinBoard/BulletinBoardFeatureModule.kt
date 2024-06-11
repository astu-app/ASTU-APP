package org.astu.feature.bulletinBoard

import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.kodein.di.DI

object BulletinBoardFeatureModule: FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
    })
}