package org.astu.feature.bulletinBoard.views.entities.announcement.creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.benasher44.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDate
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeTime
import org.astu.feature.bulletinBoard.views.entities.attachments.creation.NewSurvey
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.mappers.AudiencePresentationMapper
import kotlin.time.Duration

class CreateAnnouncementContent(audienceHierarchy: UserGroupHierarchy) {
    var textContent: MutableState<String> = mutableStateOf("")

    val delayedPublicationEnabled: MutableState<Boolean> = mutableStateOf(false)
    var delayedPublicationDateMillis: MutableState<Long>
    var delayedPublicationDateString: MutableState<String>

    var delayedPublicationTimeHours: MutableState<Int>
    var delayedPublicationTimeMinutes: MutableState<Int> = mutableStateOf(0)
    var delayedPublicationTimeString: MutableState<String>

    val delayedHidingEnabled: MutableState<Boolean> = mutableStateOf(false)
    var delayedHidingDateMillis: MutableState<Long>
    var delayedHidingDateString: MutableState<String>

    var delayedHidingTimeHours: MutableState<Int>
    var delayedHidingTimeMinutes: MutableState<Int> = mutableStateOf(0)
    var delayedHidingTimeString: MutableState<String>

    var survey: MutableState<NewSurvey?> = mutableStateOf(null)

    val audienceRoots: List<INode>
    val selectedUserIds: SnapshotStateList<Uuid> = mutableStateListOf()


    init {
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()
        delayedPublicationDateMillis = mutableStateOf(nowMillis)
        delayedPublicationDateString = mutableStateOf(humanizeDate(delayedPublicationDateMillis.value))

        val tomorrow = now + Duration.parse("1d")
        val tomorrowMillis = tomorrow.toEpochMilliseconds()
        delayedHidingDateMillis = mutableStateOf(tomorrowMillis)
        delayedHidingDateString = mutableStateOf(humanizeDate(delayedHidingDateMillis.value))

        val hourLater = now + Duration.parse("1h")

        delayedPublicationTimeHours = mutableStateOf(hourLater.toLocalDateTime(TimeZone.currentSystemDefault()).hour)
        delayedPublicationTimeString =
            mutableStateOf(humanizeTime(delayedPublicationTimeHours.value, delayedPublicationTimeMinutes.value))

        delayedHidingTimeHours = delayedPublicationTimeHours
        delayedHidingTimeString =
            mutableStateOf(humanizeTime(delayedHidingTimeHours.value, delayedHidingTimeMinutes.value))

        val audienceMapper = AudiencePresentationMapper(audienceHierarchy, selectedUserIds)
        audienceRoots = audienceMapper.mapAudienceHierarchy()
    }
}