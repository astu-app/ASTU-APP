package org.astu.infrastructure

/**
 * Модуль для инициализации DI
 */
interface FeatureModule {
    fun init(): DependencyInjector
}