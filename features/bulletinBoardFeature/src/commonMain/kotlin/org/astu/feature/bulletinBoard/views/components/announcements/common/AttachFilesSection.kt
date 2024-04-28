package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.AttachFileSummary
import org.astu.infrastructure.components.common.getButtonColors
import org.astu.infrastructure.components.extendedIcons.material.AttachFileAdd
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Секция добавления файлов
 * @param files словарь файлов в формате fileId : file
 */
@Composable
fun AttachFilesSection(files: SnapshotStateMap<Int, AttachFileSummary>) {
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
        colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
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
                    containerColor = CurrentColorScheme.tertiaryContainer
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