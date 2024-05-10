package org.astu.feature.bulletinBoard.models.entities.attachments.file.details

import com.benasher44.uuid.Uuid

data class File(
    val id: Uuid,
    val name: String,
    val type: FileType,
    val sizeInBytes: Long,
)