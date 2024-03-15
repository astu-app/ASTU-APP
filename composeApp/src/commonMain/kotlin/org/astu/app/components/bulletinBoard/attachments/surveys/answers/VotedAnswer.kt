package org.astu.app.components.bulletinBoard.attachments.surveys.answers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContent

@Composable
fun VotedAnswer(
    content: VotedAnswerContent,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(4.dp)
) {
    Row(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .requiredWidth(40.dp)
                .align(alignment = Alignment.Bottom)
        ) {
            Text(
                text = "${content.voterPercent}%",
                color = Color(0xff49454f).copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                lineHeight = 1.45.em,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(bottom = 6.dp)
        ) {
            Text(
                text = content.text,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            LinearProgressIndicator(
                progress = content.voterPercent / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
            )
        }
    }
}