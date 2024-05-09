package org.astu.feature.bulletinBoard.models.services.announcements

import kotlinx.datetime.Clock
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent

class AnnouncementCreateValidator(private val announcement: CreateAnnouncementContent) {
    fun canCreate(): Boolean {
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()

        val delayedHidingMomentMillis = announcement.delayedHidingDateMillis.value
            .plus(announcement.delayedHidingTimeHours.value * 3_600_000)
            .plus(announcement.delayedHidingTimeMinutes.value * 60_000)
        val delayedPublicationMomentMillis = announcement.delayedPublicationDateMillis.value
            .plus(announcement.delayedPublicationTimeHours.value * 3_600_000)
            .plus(announcement.delayedPublicationTimeMinutes.value * 60_000)

        return announcement.textContent.value.isNotBlank()
                && isDelayedPublicationMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isDelayedHidingMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isSurveyValid()
    }


    private fun isDelayedPublicationMomentValid(
        nowMillis: Long,
        delayedPublicationMomentMillis: Long,
        delayedHidingMomentMillis: Long
    ): Boolean {
        if (!announcement.delayedPublicationEnabled.value)
            return true

        var valid = true
        valid = valid and (nowMillis < delayedPublicationMomentMillis)

        if (announcement.delayedHidingEnabled.value) {
            valid = valid and (delayedPublicationMomentMillis < delayedHidingMomentMillis)
        }

        return valid
    }

    private fun isDelayedHidingMomentValid(
        nowMillis: Long,
        delayedPublicationMomentMillis: Long,
        delayedHidingMomentMillis: Long
    ): Boolean {
        if (!announcement.delayedHidingEnabled.value)
            return true

        var valid = true
        valid = valid and (nowMillis < delayedHidingMomentMillis)

        if (announcement.delayedPublicationEnabled.value) {
            valid = valid and (delayedPublicationMomentMillis < delayedHidingMomentMillis)
        }

        return valid
    }

    private fun isSurveyValid(): Boolean {
        val surveyValue = announcement.survey.value

        val surveyIsNotNull = surveyValue != null
        val surveyIsValid = if (surveyIsNotNull) surveyValue!!.isValid() else true
        return surveyIsValid
    }
}