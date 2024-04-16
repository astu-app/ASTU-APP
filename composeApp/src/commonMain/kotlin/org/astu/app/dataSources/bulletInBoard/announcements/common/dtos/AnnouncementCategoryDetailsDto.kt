package org.astu.app.dataSources.bulletInBoard.announcements.common.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор категории объявлений
 * @param name Название категории объявлений
 * @param color Цвет категории объявлений
 */
@Serializable
data class AnnouncementCategoryDetailsDto (
    val id: String,
    val name: String,
    val color: String
)