package org.astu.infrastructure

import kotlin.reflect.KClass

interface DependencyInjector {
    /**
     * Ленивое получение значение класса из DI
     */
    fun <T : Any> inject(clazz: KClass<*>, mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED): Lazy<T>

    /**
     * Получение значения класса из DI
     */
    fun <T : Any> get(clazz: KClass<*>): T

}

inline fun <reified T: Any> DependencyInjector.inject(){

}
