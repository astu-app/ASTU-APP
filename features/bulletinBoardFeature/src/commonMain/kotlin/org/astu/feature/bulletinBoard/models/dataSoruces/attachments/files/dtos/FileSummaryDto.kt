package org.astu.feature.bulletinBoard.models.dataSoruces.attachments.files.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор файла
 * @param name Название файла
 * @param sizeInBytes Размер файла в байтах
 * @param type Тип файла
 */
@Serializable
data class FileSummaryDto (
    val id: String,
    val name: String,
    val sizeInBytes: Long,
    val type: Int
)