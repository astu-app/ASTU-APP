package org.astu.infrastructure.DependencyInjection

import org.kodein.di.*
import org.kodein.type.erased
import kotlin.reflect.KClass

class KodeinDependencyInjector(private val di: DI) : DependencyInjector {
    /**
     * Ленивое получение значение класса из DI
     */

    override fun <T : Any> inject(clazz: KClass<T>, mode: LazyThreadSafetyMode): Lazy<T> {
        val b by di.Provider(erased(clazz))
        return lazy(mode, b)
    }

    /**
     * Получение значения класса из DI
     */
    override fun <T : Any> get(clazz: KClass<T>): T {
        val tmp by di.Instance(erased(clazz))
        return tmp
    }
}