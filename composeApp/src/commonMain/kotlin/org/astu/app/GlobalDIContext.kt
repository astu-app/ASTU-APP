package org.astu.app

import org.kodein.di.DI
import org.kodein.di.DIContainer

object GlobalDIContext : DI {
    var instance: DI? = null
        set(value) {
            if (value != null)
                field = value
        }

    /**
     * Every method eventually ends up to a call to this container.
     */
    override val container: DIContainer
        get() = instance!!.container
}