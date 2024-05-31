package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun ManageUserRightsDialog(
    label: String = "Права пользователя",
    confirmButtonLabel: String = "Применить",
    onConfirmRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onConfirmRequest,
        confirmButton = {
            Button(
                onClick = onConfirmRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CurrentColorScheme.secondaryContainer,
                    contentColor = CurrentColorScheme.onSecondaryContainer
                )
            ) {
                Text(
                    text = confirmButtonLabel,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        modifier = Modifier,
        title = { Text(text = label) },
        text = content
    )
}