package org.astu.infrastructure.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun ActionDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    modifier: Modifier = Modifier
        .wrapContentSize()
        .padding(top = 24.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
        .clip(RoundedCornerShape(32.dp))
        .background(CurrentColorScheme.surface),
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Dialog(
        onDismissRequest = onDismissRequest,
//        properties = DialogProperties(usePlatformDefaultWidth = false) // todo правильный размер в горизонт. режиме
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = CurrentColorScheme.outline
                )
            }

            content()

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(end = 12.dp, bottom = 12.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier.padding(end = 4.dp),
                    onClick = { onDismissRequest() }
                ) {
                    Text("Отменить")
                }
                OutlinedButton(
                    modifier = Modifier.padding(start = 4.dp),
                    onClick = { onConfirmRequest() }
                ) {
                    Text("ОК")
                }
            }
        }
    }
}