package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.SurveyToModelMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.dtos.UpdateIdentifierListDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.mappers.UpdateIdentifierListMappers.toDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.announcements.*
import kotlin.jvm.JvmName

object AnnouncementMappers {
    @JvmName("ContentForAnnouncementUpdatingDtoToModel")
    fun ContentForAnnouncementUpdatingDto.toModel(): ContentForAnnouncementEditing =
        ContentForAnnouncementEditing(
            id = uuidFrom(this.id),
            authorName = this.authorName,
            content = this.content,
            viewsCount = this.viewsCount,
            audienceSize = this.audienceSize,
            audienceHierarchy = this.audienceHierarchy.toModel(),
            surveys = this.surveys.toModels(),
            publishedAt = this.publishedAt,
            hiddenAt = this.hiddenAt,
            delayedHidingAt = this.delayedHidingAt,
            delayedPublishingAt = this.delayedPublishingAt,
        )

    /**
     * @param attachmentIds идентификаторы вложений в формате uuid
     */
    fun CreateAnnouncement.toDto(attachmentIds: List<String>): CreateAnnouncementDto =
        CreateAnnouncementDto(
            content = this.content,
            userIds = this.userIds,
            attachmentIds = attachmentIds,
            delayedPublishingAt = this.delayedPublishingAt,
            delayedHidingAt = this.delayedHidingAt,
        )

    fun EditAnnouncement.toDto(newAttachmentIds: List<String>?): UpdateAnnouncementDto =
        UpdateAnnouncementDto(
            id = this.id.toString(),
            content = this.content,
            audienceIds = this.users.toDto(),
            attachmentIds = UpdateIdentifierListDto(
                toAdd = newAttachmentIds?.toSet() ?: emptySet(),
                toRemove = this.attachmentIdsToRemove?.map { it.toString() }?.toSet() ?: emptySet()
            ),
            delayedPublishingAtChanged = this.delayedPublishingAtChanged,
            delayedPublishingAt = this.delayedPublishingAt,
            delayedHidingAtChanged = this.delayedHidingAtChanged,
            delayedHidingAt = this.delayedHidingAt,
        )

    @JvmName("AnnouncementSummaryDtoArrayToModels")
    fun Array<AnnouncementSummaryDto>.toModels(): List<AnnouncementSummary> =
        this.map { it.toModel() }

    @JvmName("AnnouncementSummaryDtoCollectionToModels")
    fun Collection<AnnouncementSummaryDto>.toModels(): List<AnnouncementSummary> =
        this.map { it.toModel() }

    @JvmName("AnnouncementSummaryDtoToModel")
    fun AnnouncementSummaryDto.toModel(): AnnouncementSummary =
        AnnouncementSummary(
            id = uuidFrom(this.id),
            author = this.authorName,
            publicationTime = this.publishedAt,
            text = this.content,
            viewed = this.viewsCount,
            audienceSize = this.audienceSize,
            surveys = this.surveys?.toModels() ?: emptyList()
        )

    @JvmName("AnnouncementDetailsDtoToModel")
    fun AnnouncementDetailsDto.toModel(): AnnouncementDetails =
        AnnouncementDetails(
            id = uuidFrom(this.id),
            content = this.content,
            authorName = this.authorName,
            viewsCount = this.viewsCount,
            audienceSize = this.audienceSize,
            surveys = this.surveys?.toModels() ?: emptyList(),
            publishedAt = this.publishedAt,
            hiddenAt = this.hiddenAt,
            delayedPublishingAt = this.delayedPublishingAt,
            delayedHidingAt = this.delayedHidingAt,
            audience = this.audience.toModels()
        )
}