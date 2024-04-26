package org.astu.app.dataSources.bulletInBoard.common.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorCode (
    val code: Int
)