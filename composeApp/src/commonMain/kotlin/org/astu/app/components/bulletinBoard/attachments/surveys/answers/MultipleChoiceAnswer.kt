package org.astu.app.components.bulletinBoard.attachments.surveys.answers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent

@Composable
fun MultipleChoiceAnswer(
    content: MultipleChoiceAnswerContent,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 4.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = content.selected.value,
            onCheckedChange = {
                content.selected.value = !content.selected.value
            },
        )
        Text(content.text)
    }
}