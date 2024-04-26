package org.astu.app.components.bulletinBoard.attachments.files.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.components.common.getCardColors
import org.astu.app.theme.CurrentColorScheme

/**
 * Информация о файле, прикрепляемом к создаваемому объявлению
 */
class AttachFileSummary(
    val name: String,
    val size: String,
    private val onDetachRequest: () -> Unit,
) : ContentProvider, DefaultModifierProvider {
    @Composable
    override fun Content(modifier: Modifier) {
        ElevatedCard(
            colors = Color.getCardColors(
                containerColor = CurrentColorScheme.tertiaryContainer,
            ),
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
                    modifier = Modifier
                        .requiredSize(size = 40.dp)
                        .weight(0.15f)
                ) {
                    // Иконка вложения
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