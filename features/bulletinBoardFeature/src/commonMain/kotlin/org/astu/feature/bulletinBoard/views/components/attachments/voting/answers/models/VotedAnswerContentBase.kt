package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.infrastructure.theme.CurrentColorScheme

abstract class VotedAnswerContentBase(
    id: Uuid,
    text: String,
    protected val voterPercent: Int
) : AnswerContentBase(id, text),
    DefaultModifierProvider, ContentProvider {
    @Composable
    protected open fun DrawVotedContentSummary(modifier: Modifier) {
        Row(modifier = modifier) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .requiredWidth(40.dp)
                    .align(alignment = Alignment.Bottom)
            ) {
                Text(
                    text = "$voterPercent%",
                    color = CurrentColorScheme.outline,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.45.em,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 6.dp)
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                LinearProgressIndicator(
                    progress = { voterPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(2.dp)),
                )
            }
        }
    }
}