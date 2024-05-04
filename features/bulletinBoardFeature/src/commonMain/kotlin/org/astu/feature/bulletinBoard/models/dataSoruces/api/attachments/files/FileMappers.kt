package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.dtos.FileSummaryDto
import org.astu.feature.bulletinBoard.models.entities.attachments.file.File
import org.astu.feature.bulletinBoard.models.entities.attachments.file.FileType
import kotlin.jvm.JvmName

object FileMappers {
    @JvmName("FileSummaryDtoToModel")
    fun FileSummaryDto.toModel(): File =
        File(uuidFrom(this.id), this.name, FileType.fromInt(this.type), this.sizeInBytes)

    @JvmName("FileSummaryDtoCollectionToModels")
    fun Collection<FileSummaryDto>?.toModels(): List<File> =
        this?.map { it.toModel() } ?: emptyList()
}