package org.astu.feature.bulletinBoard.models.entities.attachments.file

enum class FileType(val value: Int) {
    FILE(1),
    MEDIAFILE(2);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}