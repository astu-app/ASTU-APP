package org.astu.feature.bulletinBoard.views.entities

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.benasher44.uuid.Uuid
import kotlinx.datetime.*
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.audience.SelectableUser
import org.astu.feature.bulletinBoard.models.entities.audience.getUserGroupHierarchyMembers
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.AttachedDetachableFileContent
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.FileContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.dateTime.getDateString
import org.astu.feature.bulletinBoard.views.dateTime.getDateTimeFromEpochMillis
import org.astu.feature.bulletinBoard.views.dateTime.getDateTimeString
import org.astu.feature.bulletinBoard.views.dateTime.getTimeString
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToPresentationMappers.toPresentations
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToPresentationMappers.votedSurveyToPresentation
import org.astu.feature.bulletinBoard.views.entities.attachments.creation.NewSurvey
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.mappers.AudiencePresentationMapper
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.time.Duration

class EditAnnouncementContent(private val editContent: ContentForAnnouncementEditing) {
    val id: Uuid = editContent.id
    val author: String = editContent.authorName
    lateinit var publicationTimeString: String
    val viewed: Int = editContent.viewsCount
    val viewedPercent: Int = round(editContent.viewsCount * 1f / editContent.audienceSize).roundToInt()
    val audienceSize: Int = editContent.audienceSize
    var text: MutableState<String> = mutableStateOf(editContent.content)

    val delayedPublicationEnabled: MutableState<Boolean> = mutableStateOf(editContent.delayedPublishingAt != null)
    lateinit var delayedPublicationDateMillis: MutableState<Long>
    lateinit var delayedPublicationDateString: MutableState<String>

    lateinit var delayedPublicationTimeHours: MutableState<Int>
    lateinit var delayedPublicationTimeMinutes: MutableState<Int>
    lateinit var delayedPublicationTimeString: MutableState<String>
    val delayedPublicationAt: LocalDateTime?
        get() {
            val date = getDateTimeFromEpochMillis(delayedPublicationDateMillis.value)
            val time = LocalTime(delayedPublicationTimeHours.value, delayedPublicationTimeMinutes.value)
            return LocalDateTime(date.date, time)
        }

    val delayedHidingEnabled: MutableState<Boolean> = mutableStateOf(editContent.delayedHidingAt != null)
    lateinit var delayedHidingDateMillis: MutableState<Long>
    lateinit var delayedHidingDateString: MutableState<String>

    lateinit var delayedHidingTimeHours: MutableState<Int>
    lateinit var delayedHidingTimeMinutes: MutableState<Int>
    lateinit var delayedHidingTimeString: MutableState<String>
    val delayedHidingAt: LocalDateTime?
        get() {
            val date = getDateTimeFromEpochMillis(delayedHidingDateMillis.value)
            val time = LocalTime(delayedHidingTimeHours.value, delayedHidingTimeMinutes.value)
            return LocalDateTime(date.date, time)
        }

    val files: SnapshotStateMap<Int, FileContentBase>
    val attachedSurvey: AttachedSurveyContent?
    var newSurvey: MutableState<NewSurvey?> = mutableStateOf(null)

    val audienceRoots: List<INode>
    val selectedUserIds: MutableSet<Uuid>


    init {
        setPublicationTimeString()
        setDelayedMoments()

        files = mutableStateMapOf()
        var fileSerial = 0
        files.putAll(editContent.files
            .toPresentations()
            .associateBy ({ fileSerial++ }, { it as AttachedDetachableFileContent })
        )

        attachedSurvey = editContent.surveys?.elementAtOrNull(0)?.votedSurveyToPresentation() as AttachedSurveyContent?

        selectedUserIds = editContent.audienceHierarchy.roots
            .flatMap {
                it
                    .getUserGroupHierarchyMembers()
                    .filter { (it as SelectableUser).isSelected }
                    .map { it.id }
            }.toMutableSet()

        val audienceMapper = AudiencePresentationMapper(editContent.audienceHierarchy, selectedUserIds)
        audienceRoots = audienceMapper.mapAudienceHierarchy()
    }

    private fun setPublicationTimeString() {
        if (editContent.publishedAt == null) {
            publicationTimeString = "Еще не опубликовано"
        } else {
            publicationTimeString = "Опубликовано ${getDateTimeString(editContent.publishedAt)}"
        }
    }

    private fun setDelayedMoments() {
        val now = Clock.System.now()

        // Момент отложенной публикации. В качестве момента отложенной публикации кстанавливаем текущий момент времени,
        // если отложенная публикация не была задана.
        val delayedPublishingAt = editContent.delayedPublishingAt?.toInstant(TimeZone.currentSystemDefault()) ?: now
        val delayedPublicationDateMillis = delayedPublishingAt.toEpochMilliseconds()
        this.delayedPublicationDateMillis = mutableStateOf(delayedPublicationDateMillis)
        delayedPublicationDateString = mutableStateOf(getDateString(this.delayedPublicationDateMillis.value))

        // День, следующий за днем отложенной публикации.
        val nextDayAfterDelayedPublication = delayedPublishingAt + Duration.parse("1d")
        val nextDayAfterDelayedPublicationMillis = nextDayAfterDelayedPublication.toEpochMilliseconds()
        delayedHidingDateMillis = mutableStateOf(nextDayAfterDelayedPublicationMillis)
        delayedHidingDateString = mutableStateOf(getDateString(delayedHidingDateMillis.value))

        // Если момент отложенной публикации или момент отложенного сокрытия не задан, то в час этих моментов
        // устанавливаем час, следующий за текущим моментом времени
        val hourLater = now + Duration.parse("1h")
        val hourLaterValue = hourLater.toLocalDateTime(TimeZone.currentSystemDefault()).hour

        delayedPublicationTimeHours = mutableStateOf(editContent.delayedPublishingAt?.hour ?: hourLaterValue)
        delayedPublicationTimeMinutes = mutableStateOf(editContent.delayedPublishingAt?.minute ?: 0)
        delayedPublicationTimeString =
            mutableStateOf(getTimeString(delayedPublicationTimeHours.value, delayedPublicationTimeMinutes.value))

        delayedHidingTimeHours = mutableStateOf(editContent.delayedHidingAt?.hour ?: hourLaterValue)
        delayedHidingTimeMinutes = mutableStateOf(editContent.delayedHidingAt?.minute ?: 0)
        delayedHidingTimeString =
            mutableStateOf(getTimeString(delayedHidingTimeHours.value, delayedHidingTimeMinutes.value))
    }
}