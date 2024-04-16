package org.astu.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.theme.CurrentColorScheme

@Composable
fun LoadingFailedDialog(
    body: String,
    label: String = "Ошибка загрузки",
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                    contentColor = CurrentColorScheme.onSecondaryContainer
                )
            ) {
                Text(
                    text = "Обновить",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        modifier = Modifier,
        icon = {
            Icon(Icons.Outlined.ErrorOutline, contentDescription = null)
        },
        title = {
            Text(text = label)
        },
        text = {
            Text(text = body)
        }
    )
}