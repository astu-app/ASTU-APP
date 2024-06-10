package org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorCode (
    val code: Int
)