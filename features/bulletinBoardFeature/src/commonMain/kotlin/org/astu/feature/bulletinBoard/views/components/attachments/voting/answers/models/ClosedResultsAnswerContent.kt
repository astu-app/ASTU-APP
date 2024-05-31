package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.infrastructure.theme.CurrentColorScheme

class ClosedResultsAnswerContent(id: Uuid, text: String) : AnswerContentBase(id, text) {
    @Composable
    override fun Content(modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            IconButton(
                onClick = { },
                modifier = Modifier
                    .offset((-8).dp)
                    .scale(0.7f)
            ) {
                Icon(
                    Icons.Outlined.RadioButtonChecked,
                    null,
                    tint = CurrentColorScheme.outline,
                )
            }
            Text(text)
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(vertical = 4.dp)
    }
}