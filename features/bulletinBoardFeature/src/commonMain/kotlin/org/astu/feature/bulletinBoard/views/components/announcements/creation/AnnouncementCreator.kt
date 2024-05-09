package org.astu.feature.bulletinBoard.views.components.announcements.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.viewModels.announcements.CreateAnnouncementViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.AttachFilesSection
import org.astu.feature.bulletinBoard.views.components.announcements.common.AttachSurveySection
import org.astu.feature.bulletinBoard.views.components.announcements.common.DelayedMomentPicker
import org.astu.feature.bulletinBoard.views.components.announcements.common.SelectAudienceSection
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Класс-создатель объявления
 */
class AnnouncementCreator(
    private val viewModel: CreateAnnouncementViewModel,
) : ContentProvider, DefaultModifierProvider {
    private val announcement: MutableState<CreateAnnouncementContent?> = viewModel.content

    fun canCreate(): Boolean {
        return viewModel.canCreate()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        if (announcement.value == null)
            return

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
                        value = announcement.value!!.textContent.value,
                        onValueChange = { newText -> announcement.value!!.textContent.value = newText },
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
            AttachFilesSection(announcement.value!!.files)

            // Опрос
            AttachSurveySection(announcement.value!!.survey)

            // Аудитория
            SelectAudienceSection(announcement.value!!.audienceRoots)
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
            delayedMomentEnabled = announcement.value!!.delayedPublicationEnabled,
            dateMillis = announcement.value!!.delayedPublicationDateMillis,
            dateString = announcement.value!!.delayedPublicationDateString,
            timeHours = announcement.value!!.delayedPublicationTimeHours,
            timeMinutes = announcement.value!!.delayedPublicationTimeMinutes,
            timeString = announcement.value!!.delayedPublicationTimeString,
        )
    }

    @Composable
    private fun DelayedHidingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = "Автоматическое сокрытие",
            delayedMomentEnabled = announcement.value!!.delayedHidingEnabled,
            dateMillis = announcement.value!!.delayedHidingDateMillis,
            dateString = announcement.value!!.delayedHidingDateString,
            timeHours = announcement.value!!.delayedHidingTimeHours,
            timeMinutes = announcement.value!!.delayedHidingTimeMinutes,
            timeString = announcement.value!!.delayedHidingTimeString,
        )
    }
}