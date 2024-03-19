package org.astu.app.components.bulletinBoard.announcements.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.getName
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import com.mohamedrejeb.calf.ui.datepicker.AdaptiveDatePicker
import com.mohamedrejeb.calf.ui.datepicker.rememberAdaptiveDatePickerState
import com.mohamedrejeb.calf.ui.timepicker.AdaptiveTimePicker
import com.mohamedrejeb.calf.ui.timepicker.rememberAdaptiveTimePickerState
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.astu.app.components.ActionDialog
import org.astu.app.components.SwitchRow
import org.astu.app.components.bulletinBoard.announcements.creation.models.NewSurvey
import org.astu.app.components.bulletinBoard.attachments.files.models.AttachFileSummary
import org.astu.app.components.common.getButtonColors
import org.astu.app.components.extendedIcons.material.AttachFileAdd
import org.astu.app.theme.CurrentColorScheme
import kotlin.time.Duration

/**
 * Класс-создатель объявления
 */
class AnnouncementCreator {
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



//    @Composable
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
    fun Content(
        modifier: Modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            // Текст
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Cyan),
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
            AddFiles()

            // Опрос
            AddSurvey()
        }
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DelayedMomentPicker(
        switchTitle: String,
        delayedMomentEnabled: MutableState<Boolean>,
        dateMillis: MutableState<Long>,
        dateString: MutableState<String>,
        timeHours: MutableState<Int>,
        timeMinutes: MutableState<Int>,
        timeString: MutableState<String>,
    ) {
        val datePickerDialogOpen = remember { mutableStateOf(false) }
        val datePickerState = rememberAdaptiveDatePickerState(initialSelectedDateMillis = dateMillis.value)
        val timePickerDialogOpen = remember { mutableStateOf(false) }
        val timePickerState = rememberAdaptiveTimePickerState(
            initialHour = timeHours.value,
            initialMinute = timeMinutes.value,
            is24Hour = true
        )

        SwitchRow(
            title = switchTitle,
            delayedMomentEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        if (delayedMomentEnabled.value) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DatePickerTextField(
                    dateString.value,
                    "Дата",
                    datePickerDialogOpen,
                    Modifier.weight(1f)
                )
                DatePickerTextField(
                    timeString.value,
                    "Время",
                    timePickerDialogOpen,
                    Modifier.weight(1f)
                )
            }
            if (datePickerDialogOpen.value) {
                LaunchedEffect(datePickerState.selectedDateMillis) {
                    dateMillis.value = datePickerState.selectedDateMillis ?: 0
                    dateString.value = getDateString(dateMillis.value)
                }

                ActionDialog(
                    title = "Выбор даты",
                    onDismissRequest = { datePickerDialogOpen.value = false },
                    onConfirmRequest = {
                        dateMillis.value = datePickerState.selectedDateMillis ?: 0
                        dateString.value = getDateString(dateMillis.value)
                        datePickerDialogOpen.value = false
                    }
                ) {
                    AdaptiveDatePicker(
                        state = datePickerState,
                    )
                }
            }
            if (timePickerDialogOpen.value) {
                LaunchedEffect(timePickerState.hour, timePickerState.minute) {
                    timeHours.value = timePickerState.hour
                    timeMinutes.value = timePickerState.minute
                    timeString.value = getTimeString(timeHours.value, timeMinutes.value)
                }

                ActionDialog(
                    title = "Выбор времени",
                    onDismissRequest = { timePickerDialogOpen.value = false },
                    onConfirmRequest = {
                        timeHours.value = timePickerState.hour
                        timeMinutes.value = timePickerState.minute
                        timeString.value = getTimeString(timeHours.value, timeMinutes.value)
                        timePickerDialogOpen.value = false
                    }
                ) {
                    AdaptiveTimePicker(
                        state = timePickerState,
                        modifier = Modifier
                    )
                }
            }
        }
    }

    @Composable
    private fun DatePickerTextField(
        text: String,
        label: String,
        dialogOpen: MutableState<Boolean>,
        modifier: Modifier
    ) {
        OutlinedTextField(
            value = text,
            label = { Text(label) },
            readOnly = true,
            onValueChange = { },
            interactionSource = remember { MutableInteractionSource() } // работает, как clickable
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                dialogOpen.value = true
                            }
                        }
                    }
                },
            modifier = modifier
        )
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    private inline fun getDateString(millis: Long): String {
        val instant = Instant.fromEpochMilliseconds(millis)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("dd/MM/yyyy") }
        return dateTime.format(dateTimeFormat)
    }

    private inline fun getTimeString(hours: Int, minutes: Int): String {
        val hoursString = if (hours < 10) "0$hours" else hours.toString()
        val minutesString = if (minutes < 10) "0$minutes" else minutes.toString()
        return "$hoursString:$minutesString"
    }

    @Composable
    private fun AddFiles() {
        var lastFileId = remember { files.size }

        val scope = rememberCoroutineScope()
        val context = LocalPlatformContext.current
        val pickerLauncher = rememberFilePickerLauncher(
            type = FilePickerFileType.All,
            selectionMode = FilePickerSelectionMode.Multiple,
            onResult = { pickedFiles ->
                val id = lastFileId
                scope.launch {
                    pickedFiles.firstOrNull()?.let { file ->
                        // Do something with the selected file
                        // You can get the ByteArray of the file
                        files[id] = AttachFileSummary(
                            name = file.getName(context) ?: "name",
                            size = "size",
                        ) {
                            files.remove(id)
                        }

                        lastFileId++
                    }
                }
            }
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Cyan),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 8.dp)
            ) {
                files.forEach { (_, file) ->
                    file.Content(file.getDefaultModifier())
                }
                Button(
                    onClick = { pickerLauncher.launch() },
                    colors = Color.getButtonColors(
                        containerColor = CurrentColorScheme?.tertiaryContainer ?: Color.Cyan
                    ),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(Icons.Outlined.AttachFileAdd, null)
                        Text("Прикрепить файл")
                    }
                }
            }
        }
    }

    @Composable
    private fun AddSurvey() {
        var surveyAttached by remember { mutableStateOf(false) }
        var showDeleteSurveyDialog by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Cyan),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                if (!surveyAttached) {
                    Button(
                        onClick = {
                            survey.value = NewSurvey { showDeleteSurveyDialog = true }
                            surveyAttached = true
                        },
                        colors = Color.getButtonColors(
                            containerColor = CurrentColorScheme?.tertiaryContainer ?: Color.Cyan
                        ),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(Icons.Outlined.AddChart, null)
                            Text("Прикрепить опрос")
                        }
                    }
                } else {
                    survey.value?.Content(survey.value!!.getDefaultModifier())
                }
            }
        }

        if (showDeleteSurveyDialog) {
            AlertDialog(
                title = {
                    Text("Удалить опрос?")
                },
                text = {
                    Text("Удаление опроса нельзя отменить")
                },
                onDismissRequest = {
                    showDeleteSurveyDialog = false
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteSurveyDialog = false
                            surveyAttached = false
                            survey.value = null
                        },
                        colors = Color.getButtonColors(
                            containerColor = CurrentColorScheme?.errorContainer ?: Color.Red,
                            contentColor = CurrentColorScheme?.onErrorContainer ?: Color.Black
                        )
                    ) {
                        Text(
                            text = "Удалить",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDeleteSurveyDialog = false },
                        colors = Color.getButtonColors(
                            containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Green,
                            contentColor = CurrentColorScheme?.onSecondaryContainer ?: Color.Black
                        )
                    ) {
                        Text(
                            text = "Отменить",
                            modifier = Modifier.clickable { showDeleteSurveyDialog = false },
                            style = MaterialTheme.typography.bodyMedium,
                            color = CurrentColorScheme?.onSecondaryContainer ?: Color.Black
                        )
                    }
                },
                containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Green,
                modifier = Modifier
            )
        }
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