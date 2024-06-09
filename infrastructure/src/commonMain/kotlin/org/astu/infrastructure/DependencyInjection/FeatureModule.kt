package org.astu.infrastructure.DependencyInjection

/**
 * Модуль для инициализации DI
 */
interface FeatureModule {
    fun init(): DependencyInjector
}