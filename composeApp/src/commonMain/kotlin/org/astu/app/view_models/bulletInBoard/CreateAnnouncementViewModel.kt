package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import com.benasher44.uuid.Uuid
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.models.bulletInBoard.AnnouncementModel
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement

class CreateAnnouncementViewModel : StateScreenModel<CreateAnnouncementViewModel.State>(State.Init) {
    sealed class State {
        data object Init : State()
        data object Uploading : State()
        data object UploadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    val content: CreateAnnouncementContent = model.getCreateContent()
    val uploadedAttachments: MutableList<Uuid> = mutableListOf()


    init {
        mutableState.value = State.Init
    }

    fun canCreate(): Boolean {
        return model.canCreate(content)
    }

    fun create() {
        mutableState.value = State.Uploading
        // todo upload attachments
        model.create(toModel())

        mutableState.value = State.UploadingDone
    }

    private fun toModel(): CreateAnnouncement {
        return CreateAnnouncement(
            content = content.textContent.value,
            userIds = getUserIds(),
            attachmentIds = uploadedAttachments,
            delayedPublishingAt = getDelayedPublicationMoment(),
            delayedHidingAt = getDelayedHidingMoment(),
        )
    }

    private fun getUserIds(): List<Uuid> {
        TODO("get user ids")
    }

    private fun getDelayedPublicationMoment(): LocalDateTime {
        val dateMillis = content.delayedPublicationDateMillis.value
        val hour = content.delayedPublicationTimeHours.value
        val minute = content.delayedPublicationTimeMinutes.value

        return getDelayedMoment(dateMillis, hour, minute)
    }

    private fun getDelayedHidingMoment(): LocalDateTime {
        val dateMillis = content.delayedHidingDateMillis.value
        val hour = content.delayedHidingTimeHours.value
        val minute = content.delayedHidingTimeMinutes.value

        return getDelayedMoment(dateMillis, hour, minute)
    }

    private fun getDelayedMoment(dateMillis: Long, delayedTimeHours: Int, delayedTimeMinutes: Int): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(dateMillis)
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val delayedHidingAt = LocalDateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hour = delayedTimeHours,
            minute = delayedTimeMinutes,
        )

        return delayedHidingAt
    }
}