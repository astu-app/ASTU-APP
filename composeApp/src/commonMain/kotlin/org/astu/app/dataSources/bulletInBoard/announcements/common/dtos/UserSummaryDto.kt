package org.astu.app.dataSources.bulletInBoard.announcements.common.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор
 * @param firstName Имя
 * @param secondName Фамилия
 * @param patronymic Отчество
 */
@Serializable
data class UserSummaryDto (
    val id: String,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
)