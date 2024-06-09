package org.astu.infrastructure.DependencyInjection

import kotlin.reflect.KClass

object GlobalDIContext {
    /**
     * Модули DI
     */
    val modules: MutableList<DependencyInjector> = mutableListOf()

    /**
     * Добавления модуля DI
     */
    fun addModule(dependencyInjector: DependencyInjector) {
        modules.add(dependencyInjector)
    }

    /**
     * Ленивая выдача значения класса
     */
    inline fun <reified T : Any> inject(
        clazz: KClass<T> = T::class,
        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.SYNCHRONIZED
    ): Lazy<T> {
        return promotion(clazz) { it.inject(clazz, mode) }
    }

    /**
     * Выдача значения класса
     */
    inline fun <reified T : Any> get(clazz: KClass<T> = T::class): T {
        return promotion(clazz) { it.get(clazz) }
    }

    /**
     * Обход всех модулей на наличие объявления класса
     */
    inline fun <reified T : Any> promotion(clazz: KClass<*>, action: (DependencyInjector) -> T): T {
        for (dependencyInjector in modules) {
            try {
                return action.invoke(dependencyInjector)
            } catch (_: Exception) {
            }
        }
        TODO("Не было найдено объявление класса ${clazz.simpleName}")
    }
}