package org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.dtos

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