package org.astu.infrastructure.DependencyInjection

import kotlin.reflect.KClass

interface DependencyInjector {
    /**
     * Ленивое получение значение класса из DI
     */
    fun <T : Any> inject(clazz: KClass<T>, mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED): Lazy<T>

    /**
     * Получение значения класса из DI
     */
    fun <T : Any> get(clazz: KClass<T>): T
}