package org.astu.app.models.bulletInBoard.entities.attachments.file

import com.benasher44.uuid.Uuid

data class File(
    val id: Uuid,
    val name: String,
    val type: FileType,
    val sizeInBytes: Long,
)