package org.astu.app.entities.bulletInBoard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.app.components.bulletinBoard.announcements.creation.models.NewSurvey
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.INode
import org.astu.app.components.bulletinBoard.attachments.files.models.AttachFileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.common.models.UserGroupStorage
import org.astu.app.utils.dateTime.getDateString
import org.astu.app.utils.dateTime.getTimeString
import kotlin.time.Duration

class EditAnnouncementContent {
    val id: Uuid = uuidFrom("a4d5b2ba-1a10-4f94-bcc5-744d5a439568")
    val author: String = "Белов Сергей Валерьевич"
    val publicationTimeString: String = "Опубликовано 15 фев 15:50"
    val viewed: Int = 145
    val viewedPercent: Int = 48
    val audienceSize: Int = 300

    var text: MutableState<String> = mutableStateOf("Boulder persian newsletter northwest flavor possess painting, mobility caused internship hypothetical closest change breakdown, fork accepts browsing running finally sensors cet, plan basically waters sent.")

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

    val files: SnapshotStateMap<Int, AttachFileSummary> = mutableStateMapOf()
    var attachedSurvey: SurveyContent? = null
    var newSurvey: MutableState<NewSurvey?> = mutableStateOf(null)

    val audienceRoot: INode = UserGroupStorage.makeSelectableAudience()


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
    }
}