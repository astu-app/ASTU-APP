package org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.RadioButtonsStateKeeper

@Composable
fun SingleChoiceAnswer(
    stateId: Int,
    stateKeeper: RadioButtonsStateKeeper,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.CenterVertically)
        .padding(vertical = 4.dp)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = stateKeeper.getSelected() == stateId,
            onClick = {
                stateKeeper.selectedStateId = stateId
            }
        )
        Text(
            text = stateKeeper.getState(stateId)?.text ?: "Не удалось получить текст ответа",
        )
    }
}