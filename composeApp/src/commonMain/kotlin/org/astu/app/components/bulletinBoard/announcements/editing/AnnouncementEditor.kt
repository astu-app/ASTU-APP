package org.astu.app.components.bulletinBoard.announcements.editing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.announcements.common.AttachFilesSection
import org.astu.app.components.bulletinBoard.announcements.common.AttachSurveySection
import org.astu.app.components.bulletinBoard.announcements.common.DelayedMomentPicker
import org.astu.app.components.bulletinBoard.announcements.details.AnnouncementDetailsHeader
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.theme.CurrentColorScheme
import org.astu.app.view_models.bulletInBoard.EditAnnouncementViewModel

class AnnouncementEditor(
    private val viewModel: EditAnnouncementViewModel,
) : ContentProvider, DefaultModifierProvider {
    private val announcement: EditAnnouncementContent = viewModel.content

    fun canEdit(): Boolean {
        return viewModel.canEdit()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier,
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(all = 12.dp)
                ) {
                    // Заголовок
                    AnnouncementDetailsHeader(
                        authorName = announcement.author,
                        publicationMoment = announcement.publicationTimeString,
                        viewed = announcement.viewed,
                        viewedPercent = announcement.viewedPercent,
                        audienceSize = announcement.audienceSize
                    )

                    // Текст
                    OutlinedTextField(
                        value = announcement.text.value,
                        onValueChange = { announcement.text.value = it },
                    )

                    // Отложенная публикация и сокрытие
                    DelayedPublishingMomentSetter()
                    DelayedHidingMomentSetter()
                }
            }

            // Файлы
            AttachFilesSection(announcement.files)

            // Опрос
            val attachedSurvey = announcement.attachedSurvey
            if (attachedSurvey != null) {
                attachedSurvey.Content(attachedSurvey.getDefaultModifier())
            } else {
                AttachSurveySection(announcement.newSurvey)
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 8.dp)
    }



    @Composable
    private fun DelayedPublishingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = "Автоматическая публикация",
            delayedMomentEnabled = announcement.delayedPublicationEnabled,
            dateMillis = announcement.delayedPublicationDateMillis,
            dateString = announcement.delayedPublicationDateString,
            timeHours = announcement.delayedPublicationTimeHours,
            timeMinutes = announcement.delayedPublicationTimeMinutes,
            timeString = announcement.delayedPublicationTimeString,
        )
    }

    @Composable
    private fun DelayedHidingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = "Автоматическое сокрытие",
            delayedMomentEnabled = announcement.delayedHidingEnabled,
            dateMillis = announcement.delayedHidingDateMillis,
            dateString = announcement.delayedHidingDateString,
            timeHours = announcement.delayedHidingTimeHours,
            timeMinutes = announcement.delayedHidingTimeMinutes,
            timeString = announcement.delayedHidingTimeString,
        )
    }
}