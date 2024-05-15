package org.astu.feature.bulletinBoard.views.entities.announcement.creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.benasher44.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.CurrentlyAttachedFileContent
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.FileContentBase
import org.astu.feature.bulletinBoard.views.dateTime.getDateString
import org.astu.feature.bulletinBoard.views.dateTime.getTimeString
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

    var files: SnapshotStateMap<Int, FileContentBase> = mutableStateMapOf()
    var survey: MutableState<NewSurvey?> = mutableStateOf(null)

    val audienceRoots: List<INode>
    val selectedUserIds: SnapshotStateList<Uuid> = mutableStateListOf()


    init {
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()
        delayedPublicationDateMillis = mutableStateOf(nowMillis)
        delayedPublicationDateString = mutableStateOf(getDateString(delayedPublicationDateMillis.value))

        val tomorrow = now + Duration.parse("1d")
        val tomorrowMillis = tomorrow.toEpochMilliseconds()
        delayedHidingDateMillis = mutableStateOf(tomorrowMillis)
        delayedHidingDateString = mutableStateOf(getDateString(delayedHidingDateMillis.value))

        val hourLater = now + Duration.parse("1h")

        delayedPublicationTimeHours = mutableStateOf(hourLater.toLocalDateTime(TimeZone.currentSystemDefault()).hour)
        delayedPublicationTimeString =
            mutableStateOf(getTimeString(delayedPublicationTimeHours.value, delayedPublicationTimeMinutes.value))

        delayedHidingTimeHours = delayedPublicationTimeHours
        delayedHidingTimeString =
            mutableStateOf(getTimeString(delayedHidingTimeHours.value, delayedHidingTimeMinutes.value))

        files[0] = CurrentlyAttachedFileContent("Документ.docx", "20 мб") { files.remove(0) }
        files[1] = CurrentlyAttachedFileContent("Презентация.pptx", "20 мб") { files.remove(1) }
        files[2] = CurrentlyAttachedFileContent("Таблица.xlsx", "20 мб") { files.remove(2) }

        val audienceMapper = AudiencePresentationMapper(audienceHierarchy, selectedUserIds)
        audienceRoots = audienceMapper.mapAudienceHierarchy()
    }
}