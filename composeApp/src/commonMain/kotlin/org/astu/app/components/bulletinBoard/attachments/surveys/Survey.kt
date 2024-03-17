package org.astu.app.components.bulletinBoard.attachments.surveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent

@Composable
fun Survey(
    content: SurveyContent,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Column(modifier = modifier) {
        PagedQuestions(content.questions)
        VoteButton()
    }
}