package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.theme.CurrentColorScheme

abstract class VotedAnswerContentBase(text: String, private val voterPercent: Int) : AnswerContentBase(text),
    DefaultModifierProvider, ContentProvider {
    @Composable
    protected fun DrawVotedContentSummary(modifier: Modifier) {
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
                    color = CurrentColorScheme?.outline ?: Color.Gray,
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