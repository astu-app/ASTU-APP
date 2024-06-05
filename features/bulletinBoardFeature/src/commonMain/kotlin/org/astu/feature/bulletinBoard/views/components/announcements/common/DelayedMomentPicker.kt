package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.calf.ui.datepicker.AdaptiveDatePicker
import com.mohamedrejeb.calf.ui.datepicker.rememberAdaptiveDatePickerState
import com.mohamedrejeb.calf.ui.timepicker.AdaptiveTimePicker
import com.mohamedrejeb.calf.ui.timepicker.rememberAdaptiveTimePickerState
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDate
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeTime
import org.astu.infrastructure.components.ActionDialog
import org.astu.infrastructure.components.SwitchRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DelayedMomentPicker(
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
                dateString.value = humanizeDate(dateMillis.value)
            }

            ActionDialog(
                title = "Выбор даты",
                onDismissRequest = { datePickerDialogOpen.value = false },
                onConfirmRequest = {
                    dateMillis.value = datePickerState.selectedDateMillis ?: 0
                    dateString.value = humanizeDate(dateMillis.value)
                    datePickerDialogOpen.value = false
                }
            ) {
                AdaptiveDatePicker(
                    state = datePickerState, // todo добавить заголовок диалога
                )
            }
        }
        if (timePickerDialogOpen.value) {
            LaunchedEffect(timePickerState.hour, timePickerState.minute) {
                timeHours.value = timePickerState.hour
                timeMinutes.value = timePickerState.minute
                timeString.value = humanizeTime(timeHours.value, timeMinutes.value)
            }

            ActionDialog(
                title = "Выбор времени",
                onDismissRequest = { timePickerDialogOpen.value = false },
                onConfirmRequest = {
                    timeHours.value = timePickerState.hour
                    timeMinutes.value = timePickerState.minute
                    timeString.value = humanizeTime(timeHours.value, timeMinutes.value)
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