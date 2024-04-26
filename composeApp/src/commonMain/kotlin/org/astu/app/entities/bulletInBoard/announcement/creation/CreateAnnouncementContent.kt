package org.astu.app.entities.bulletInBoard.announcement.creation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.benasher44.uuid.Uuid
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.app.components.bulletinBoard.attachments.files.models.AttachFileSummary
import org.astu.app.entities.bulletInBoard.audienceGraph.INode
import org.astu.app.infrastructure.mappers.presentation.AudiencePresentationMapper
import org.astu.app.infrastructure.utils.dateTime.getDateString
import org.astu.app.infrastructure.utils.dateTime.getTimeString
import org.astu.app.models.bulletInBoard.entities.audience.AudienceHierarchy
import kotlin.time.Duration

class CreateAnnouncementContent(audienceHierarchy: AudienceHierarchy) {
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

    var files: SnapshotStateMap<Int, AttachFileSummary> = mutableStateMapOf()
    var survey: MutableState<NewSurvey?> = mutableStateOf(null)

    val audienceRoots: List<INode>
    val selectedUserIds: MutableSet<Uuid> = mutableSetOf()


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

        files[0] = AttachFileSummary("Документ.docx", "20 мб") { files.remove(0) }
        files[1] = AttachFileSummary("Презентация.pptx", "20 мб") { files.remove(1) }
        files[2] = AttachFileSummary("Таблица.xlsx", "20 мб") { files.remove(2) }

        val audienceMapper = AudiencePresentationMapper(audienceHierarchy, selectedUserIds)
        audienceRoots = audienceMapper.mapAudienceHierarchy()
    }
}