package org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.dtos

import kotlinx.serialization.Serializable

/**
 * DTO для создания варианта ответа
 * @param serial Порядковый номер варианта ответа в списке вариантов ответов
 * @param content Текстовое содержимое варианта ответа
 */
@Serializable
data class CreateAnswerDto (
    val serial: Int,
    val content: String,
)