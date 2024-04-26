package org.astu.app.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.theme.CurrentColorScheme

@Composable
fun ActionFailedDialog(
    body: String,
    label: String = "Ошибка загрузки",
    tryAgainButtonLabel: String = "Повторить",
    onTryAgainRequest: () -> Unit,
    dismissButtonLabel: String = "Отменить",
    onDismissRequest: (() -> Unit)? = null,
    showDismissButton: Boolean = true,
) {
    AlertDialog(
        // при клике вне диалогового окна действие будет повторяться
        onDismissRequest = onTryAgainRequest,
        confirmButton = {
            Button(
                onClick = onTryAgainRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                    contentColor = CurrentColorScheme.onSecondaryContainer
                )
            ) {
                Text(
                    text = tryAgainButtonLabel,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            if (!showDismissButton)
                return@AlertDialog

            Button(
                onClick = {
                    onDismissRequest?.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CurrentColorScheme.errorContainer,
                    contentColor = CurrentColorScheme.onErrorContainer
                )
            ) {
                Text(
                    text = dismissButtonLabel,
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