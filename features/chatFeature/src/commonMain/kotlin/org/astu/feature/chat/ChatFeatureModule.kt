package org.astu.feature.chat

import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

object ChatFeatureModule : FeatureModule {
    override fun init(): DependencyInjector = KodeinDependencyInjector(DI {
        bind<ChatRepository>() with singleton { AstuChatRepository() }
        bind<ChatServiceConfig>() with singleton { ChatServiceConfig("http://localhost:8091/") }
    })
}