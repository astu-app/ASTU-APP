package org.astu.feature.bulletinBoard.models.repositories

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiDelayedAnnouncementsDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiGeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiHiddenAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiPublishedAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryMappers.toPresentations

class AnnouncementRepository {
    private val publishedAnnouncementsSource = ApiPublishedAnnouncementDataSource()
    private val delayedAnnouncementsSource = ApiDelayedAnnouncementsDataSource()
    private val hiddenAnnouncementsSource = ApiHiddenAnnouncementDataSource()
    private val generalAnnouncementsSource = ApiGeneralAnnouncementDataSource()

    suspend fun loadList(): ContentWithError<List<AnnouncementSummaryContent>, GetPostedAnnouncementListErrors> {
        val content = publishedAnnouncementsSource.getList()
        val announcements = content.content?.toPresentations()

        return ContentWithError(announcements, content.error)
    }

    suspend fun loadDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        return publishedAnnouncementsSource.getDetails(id)
    }

    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrorsAggregate? {
        return generalAnnouncementsSource.create(announcement)
    }

    suspend fun loadEditContent(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors> {
        return generalAnnouncementsSource.getUpdateForAnnouncementEditing(id)
    }

    suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrorsAggregate? {
        return generalAnnouncementsSource.edit(announcement)
    }

    suspend fun loadHiddenList(): ContentWithError<List<AnnouncementSummaryContent>, GetHiddenAnnouncementListErrors> {
        val content = hiddenAnnouncementsSource.getList()
        val announcements = content.content?.toPresentations()

        return ContentWithError(announcements, content.error)
    }

    suspend fun delete(id: Uuid): DeleteAnnouncementErrors? {
        return generalAnnouncementsSource.delete(id)
    }

    suspend fun restore(id: Uuid): RestoreHiddenAnnouncementErrors? {
        return hiddenAnnouncementsSource.restore(id)
    }

    suspend fun hide(id: Uuid): HidePostedAnnouncementErrors? {
        return publishedAnnouncementsSource.hide(id)
    }

    suspend fun getDelayedPublishedAnnouncementList(): ContentWithError<List<AnnouncementSummaryContent>, GetDelayedPublishedAnnouncementsErrors> {
        val content = delayedAnnouncementsSource.getDelayedPublishingList()
        val announcements = content.content?.toPresentations()

        return ContentWithError(announcements, content.error)
    }

    suspend fun publishImmediately(id: Uuid): PublishImmediatelyDelayedAnnouncementErrors? {
        return delayedAnnouncementsSource.publishImmediately(id)
    }

    suspend fun getDelayedHiddenAnnouncementList(): ContentWithError<List<AnnouncementSummaryContent>, GetDelayedHiddenAnnouncementListErrors> {
        val content = delayedAnnouncementsSource.getDelayedHiddenList()
        val announcements = content.content?.toPresentations()

        return ContentWithError(announcements, content.error)
    }
}