package org.astu.app.components.bulletinBoard.announcements.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.app.components.bulletinBoard.announcements.common.AttachFilesSection
import org.astu.app.components.bulletinBoard.announcements.common.AttachSurveySection
import org.astu.app.components.bulletinBoard.announcements.common.DelayedMomentPicker
import org.astu.app.components.bulletinBoard.announcements.creation.models.NewSurvey
import org.astu.app.components.bulletinBoard.attachments.files.models.AttachFileSummary
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.theme.CurrentColorScheme
import org.astu.app.utils.dateTime.getDateString
import org.astu.app.utils.dateTime.getTimeString
import kotlin.time.Duration

/**
 * Класс-создатель объявления
 */
class AnnouncementCreator : ContentProvider, DefaultModifierProvider {
    private var textContent: MutableState<String> = mutableStateOf("")

    private val delayedPublicationEnabled: MutableState<Boolean> = mutableStateOf(false)
    private var delayedPublicationDateMillis: MutableState<Long>
    private var delayedPublicationDateString: MutableState<String>

    private var delayedPublicationTimeHours: MutableState<Int>
    private var delayedPublicationTimeMinutes: MutableState<Int> = mutableStateOf(0)
    private var delayedPublicationTimeString: MutableState<String>

    private val delayedHidingEnabled: MutableState<Boolean> = mutableStateOf(false)
    private var delayedHidingDateMillis: MutableState<Long>
    private var delayedHidingDateString: MutableState<String>

    private var delayedHidingTimeHours: MutableState<Int>
    private var delayedHidingTimeMinutes: MutableState<Int> = mutableStateOf(0)
    private var delayedHidingTimeString: MutableState<String>

    private var files: SnapshotStateMap<Int, AttachFileSummary> = mutableStateMapOf()
    private var survey: MutableState<NewSurvey?> = mutableStateOf(null)


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



    fun canCreate(): Boolean {
        val now = Clock.System.now()
        val nowMillis = now.toEpochMilliseconds()

        val delayedHidingMomentMillis = delayedHidingDateMillis.value
            .plus(delayedHidingTimeHours.value * 3_600_000)
            .plus(delayedHidingTimeMinutes.value * 60_000)
        val delayedPublicationMomentMillis = delayedPublicationDateMillis.value
            .plus(delayedPublicationTimeHours.value * 3_600_000)
            .plus(delayedPublicationTimeMinutes.value * 60_000)

        return textContent.value.isNotBlank()
                && isDelayedPublicationMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isDelayedHidingMomentValid(nowMillis, delayedPublicationMomentMillis, delayedHidingMomentMillis)
                && isSurveyValid()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            // Текст
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(all = 12.dp)
                ) {
                    OutlinedTextField(
                        value = textContent.value,
                        onValueChange = { newText -> textContent.value = newText },
                        label = { Text("Объявление") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )


                    // Отложенная публикация и сокрытие
                    DelayedPublishingMomentSetter()
                    DelayedHidingMomentSetter()
                }
            }

            // Файлы
            AttachFilesSection(files)

            // Опрос
            AttachSurveySection(survey)
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 8.dp)
    }



    @Composable
    private fun DelayedPublishingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = "Автоматическая публикация",
            delayedMomentEnabled = delayedPublicationEnabled,
            dateMillis = delayedPublicationDateMillis,
            dateString = delayedPublicationDateString,
            timeHours = delayedPublicationTimeHours,
            timeMinutes = delayedPublicationTimeMinutes,
            timeString = delayedPublicationTimeString,
        )
    }

    @Composable
    private fun DelayedHidingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = "Автоматическое сокрытие",
            delayedMomentEnabled = delayedHidingEnabled,
            dateMillis = delayedHidingDateMillis,
            dateString = delayedHidingDateString,
            timeHours = delayedHidingTimeHours,
            timeMinutes = delayedHidingTimeMinutes,
            timeString = delayedHidingTimeString,
        )
    }

    private fun isDelayedPublicationMomentValid(
        nowMillis: Long,
        delayedPublicationMomentMillis: Long,
        delayedHidingMomentMillis: Long
    ): Boolean {
        if (!delayedPublicationEnabled.value)
            return true

        var valid = true
        valid = valid and (nowMillis < delayedPublicationMomentMillis)

        if (delayedHidingEnabled.value) {
            valid = valid and (delayedPublicationMomentMillis < delayedHidingMomentMillis)
        }

        return valid
    }

    private fun isDelayedHidingMomentValid(
        nowMillis: Long,
        delayedPublicationMomentMillis: Long,
        delayedHidingMomentMillis: Long
    ): Boolean {
        if (!delayedHidingEnabled.value)
            return true

        var valid = true
        valid = valid and (nowMillis < delayedHidingMomentMillis)

        if (delayedPublicationEnabled.value) {
            valid = valid and (delayedPublicationMomentMillis < delayedHidingMomentMillis)
        }

        return valid
    }

    private fun isSurveyValid(): Boolean {
        val surveyValue = survey.value

        val surveyIsNotNull = surveyValue != null
        val surveyIsValid = if (surveyIsNotNull) surveyValue!!.isValid() else true
        return surveyIsValid
    }
}