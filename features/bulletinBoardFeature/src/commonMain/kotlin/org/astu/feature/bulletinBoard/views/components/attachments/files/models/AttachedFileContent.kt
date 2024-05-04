package org.astu.feature.bulletinBoard.views.components.attachments.files.models

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Downloading
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileDownloadDone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Файл, который уже выб прикреплен к обьъявлению
 */
class AttachedFileContent(
    val id: Uuid,
    name: String,
    size: String,
    val downloadState: MutableState<FileDownloadState>
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.requiredSize(size = 40.dp)
                ) {
                    val icon = when (downloadState.value) {
                        FileDownloadState.NOT_DOWNLOADED -> Icons.Outlined.FileDownload
                        FileDownloadState.DOWNLOADING -> Icons.Outlined.Downloading
                        FileDownloadState.DOWNLOADED -> Icons.Outlined.FileDownloadDone
                    }
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
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