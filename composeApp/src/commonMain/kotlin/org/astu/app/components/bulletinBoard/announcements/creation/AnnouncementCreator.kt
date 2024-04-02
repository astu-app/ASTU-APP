package org.astu.app.components.bulletinBoard.announcements.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.announcements.common.AttachFilesSection
import org.astu.app.components.bulletinBoard.announcements.common.AttachSurveySection
import org.astu.app.components.bulletinBoard.announcements.common.DelayedMomentPicker
import org.astu.app.components.bulletinBoard.announcements.common.SelectAudienceSection
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.theme.CurrentColorScheme
import org.astu.app.view_models.bulletInBoard.CreateAnnouncementViewModel

/**
 * Класс-создатель объявления
 */
class AnnouncementCreator(
    private val viewModel: CreateAnnouncementViewModel,
) : ContentProvider, DefaultModifierProvider {
    private val announcement: CreateAnnouncementContent = viewModel.content

    fun canCreate(): Boolean {
        return viewModel.canCreate()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            // Текст
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(all = 12.dp)
                ) {
                    OutlinedTextField(
                        value = announcement.textContent.value,
                        onValueChange = { newText -> announcement.textContent.value = newText },
                        label = { Text("Объявление") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )

                    // Отложенная публикация и сокрытие
                    DelayedPublishingMomentSetter()
                    DelayedHidingMomentSetter()
                }
            }

            // Файлы
            AttachFilesSection(announcement.files)

            // Опрос
            AttachSurveySection(announcement.survey)

            // Аудитория
            SelectAudienceSection(announcement.audienceRoot)
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