package org.astu.infrastructure

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.gateway.apis.AccountApi
import org.astu.infrastructure.gateway.models.AccountDTO

class AccountUser {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val instance: MutableState<AccountDTO?> = mutableStateOf(null)

    suspend fun checkPerm(condition: (AccountDTO) -> Boolean): Boolean {
        val client = AccountApi(config.url)
        runCatching {
            client.apiAccountGet()
        }.onSuccess {
            instance.value = it
            return condition(it)
        }.onFailure {
            println(it)
            println(it.message)
            if (instance.value != null)
                return condition(instance.value!!)
        }
        return false
    }

    suspend fun current(): AccountDTO? {
        if (instance.value != null)
            return instance.value

        println(instance.value)

        val client = AccountApi(config.url)
        runCatching {
            client.apiAccountGet()
        }.onSuccess {
            println(it)
            instance.value = it
        }.onFailure {
            println(it)
        }
        return instance.value
    }

    fun logout() {
        instance.value = null
    }
}

