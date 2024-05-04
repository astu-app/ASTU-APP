package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyContentBase
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun AttachedSurveySection(
    survey: SurveyContentBase,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 24.dp,
                    bottom = 12.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Опрос",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = CurrentColorScheme.outlineVariant
            )

            survey.Content(survey.getDefaultModifier())
        }
    }
}