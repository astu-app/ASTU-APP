package org.astu.infrastructure.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Строка с лидирующим чекбоксом
 * @param title текст строки после чекбокса
 * @param state состояние чекбокса
 * @param modifier модификатор всей строки
 * @param onCheckedStateChange действияЮ выполняемые при изменении чекбокса помимо изменения состояния (state) чекбокса
 */
@Composable
fun CheckboxRow(
    title: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onCheckedStateChange: ((Boolean) -> Unit)? = null,
) {
    CheckboxRow(
        title = { Text(title) },
        state = state,
        modifier = modifier,
        onCheckedStateChange = onCheckedStateChange,
    )
}

/**
 * Строка с лидирующим чекбоксом
 * @param title контент строки после чекбокса
 * @param state состояние чекбокса
 * @param modifier модификатор всей строки
 * @param onCheckedStateChange действияЮ выполняемые при изменении чекбокса помимо изменения состояния (state) чекбокса
 */
@Composable
fun CheckboxRow(
    title: @Composable () -> Unit,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onCheckedStateChange: ((Boolean) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.1f)
        ) {
            Checkbox(
                checked = state.value,
                onCheckedChange = {
                    state.value = it
                    if (onCheckedStateChange != null) {
                        onCheckedStateChange(it)
                    }
                },
                modifier = Modifier.height(15.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            title()
        }
    }
}