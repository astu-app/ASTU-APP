package org.astu.feature.bulletinBoard.views.components.attachments.files.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Файл, который только что прикрепили
 */
class CurrentlyAttachedFileContent(
    name: String,
    size: String,
    val onDetachRequest: () -> Unit,
) : FileContentBase(name, size) {
    @Composable
    override fun Content(modifier: Modifier) {
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.tertiaryContainer),
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 8.dp)
            ) {
                // Иконка файла
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.requiredSize(size = 40.dp)
                ) {
                    Icon(
                        Icons.Outlined.AttachFile,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Название файла и его размер
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(0.7f)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                    Text(
                        text = size,
                        color = CurrentColorScheme.outline,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }

                // Открепление файла
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .requiredSize(size = 40.dp)
                        .clickable { onDetachRequest() }
                        .weight(0.15f)
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp)
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    }
}