package org.astu.app.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SwitchRow(
    title: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    SwitchRow(
        title = { Text(title) },
        state = state,
        modifier = modifier
    )
}

@Composable
fun SwitchRow(
    title: @Composable () -> Unit,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            title()
        }

        Row (
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(0.1f)
        ) {
            Switch(
                checked = state.value,
                onCheckedChange = { state.value = it }
            )
        }
    }
}