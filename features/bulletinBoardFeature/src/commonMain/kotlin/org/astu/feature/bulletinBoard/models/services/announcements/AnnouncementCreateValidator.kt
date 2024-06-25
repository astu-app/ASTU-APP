package org.astu.feature.bulletinBoard.models.services.announcements

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent

class AnnouncementCreateValidator(private val announcement: CreateAnnouncementContent) {
    fun canCreate(): Boolean {
        val nowUtc = Clock.System.now()
        val timeZoneDifferenceMillis = TimeZone.currentSystemDefault().offsetAt(nowUtc).totalSeconds * 1_000
        val nowMillis = nowUtc.toEpochMilliseconds() + timeZoneDifferenceMillis

        val delayedHidingMomentMillis = announcement.delayedHidingDateMillis.value
            .plus(announcement.delayedHidingTimeHours.value * 3_600_000)
            .plus(announcement.delayedHidingTimeMinutes.value * 60_000)
        val delayedPublicationMomentMillis = announcement.delayedPublicationDateMillis.value
            .plus(announcement.delayedPublicationTimeHours.value * 3_600_000)
            .plus(announcement.delayedPublicationTimeMinutes.value * 60_000)

        return announcement.textContent.value.isNotBlank()
                && announcement.selectedUserIds.isNotEmpty()
                && isDelayedPublicationMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isDelayedHidingMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isSurveyValid(nowMillis)
    }


    private fun isDelayedPublicationMomentValid(
        nowMillis: Long,
        delayedPublicationMomentMillis: Long,
        delayedHidingMomentMillis: Long
    ): Boolean {
        if (!announcement.delayedPublicationEnabled.value)
            return true

        println("now ${Instant.fromEpochMilliseconds(nowMillis)}")
        println("pub ${Instant.fromEpochMilliseconds(delayedPublicationMomentMillis)}")
        println("hid ${Instant.fromEpochMilliseconds(delayedHidingMomentMillis)}")

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

    private fun isSurveyValid(nowMillis: Long): Boolean {
        val surveyValue = announcement.survey.value

        val surveyIsNotNull = surveyValue != null
        val surveyIsValid = if (surveyIsNotNull) surveyValue!!.isValid(nowMillis) else true
        return surveyIsValid
    }
}