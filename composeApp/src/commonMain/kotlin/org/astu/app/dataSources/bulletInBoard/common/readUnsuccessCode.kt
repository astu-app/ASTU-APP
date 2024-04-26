package org.astu.app.dataSources.bulletInBoard.common

import io.ktor.client.call.*
import io.ktor.client.statement.*
import org.astu.app.dataSources.bulletInBoard.common.responses.ErrorCode
import kotlin.enums.enumEntries

@OptIn(ExperimentalStdlibApi::class)
suspend inline fun <reified T> readUnsuccessCode(response: HttpResponse): T?
        where T : Enum<T> {
    val error = response.body<ErrorCode>()
    return enumEntries<T>().firstOrNull { it.ordinal + 1 == error.code }
}