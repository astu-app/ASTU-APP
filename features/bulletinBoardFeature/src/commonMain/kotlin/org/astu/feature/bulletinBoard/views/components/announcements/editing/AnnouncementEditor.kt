package org.astu.feature.bulletinBoard.views.components.announcements.editing

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.viewModels.announcements.actions.EditAnnouncementViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.AttachSurveySection
import org.astu.feature.bulletinBoard.views.components.announcements.common.AttachedSurveySection
import org.astu.feature.bulletinBoard.views.components.announcements.common.DelayedMomentPicker
import org.astu.feature.bulletinBoard.views.components.announcements.common.DisplayAudienceHierarchySection
import org.astu.feature.bulletinBoard.views.components.announcements.details.AnnouncementDetailsHeader
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.infrastructure.theme.CurrentColorScheme

class AnnouncementEditor(
    private val viewModel: EditAnnouncementViewModel,
) : ContentProvider, DefaultModifierProvider {
    fun canEdit(): Boolean =
        viewModel.canEdit()

    @Composable
    override fun Content(modifier: Modifier) {
        val announcementSnapshot = viewModel.content.value
        if (announcementSnapshot == null || viewModel.state.value != EditAnnouncementViewModel.State.EditingAnnouncement)
            return

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
                        authorName = announcementSnapshot.author,
                        publicationMoment = announcementSnapshot.publicationTimeString,
                        viewed = announcementSnapshot.viewed,
                        viewedPercent = announcementSnapshot.viewedPercent,
                        audienceSize = announcementSnapshot.audienceSize
                    )

                    // Текст
                    OutlinedTextField(
                        value = announcementSnapshot.text.value,
                        onValueChange = { announcementSnapshot.text.value = it },
                    )

                    // Отложенная публикация и сокрытие
                    DelayedMomentPicker(
                        switchTitle = "Автоматическая публикация",
                        delayedMomentEnabled = announcementSnapshot.delayedPublicationEnabled,
                        dateMillis = announcementSnapshot.delayedPublicationDateMillis,
                        dateString = announcementSnapshot.delayedPublicationDateString,
                        timeHours = announcementSnapshot.delayedPublicationTimeHours,
                        timeMinutes = announcementSnapshot.delayedPublicationTimeMinutes,
                        timeString = announcementSnapshot.delayedPublicationTimeString,
                    )
                    DelayedMomentPicker(
                        switchTitle = "Автоматическое сокрытие",
                        delayedMomentEnabled = announcementSnapshot.delayedHidingEnabled,
                        dateMillis = announcementSnapshot.delayedHidingDateMillis,
                        dateString = announcementSnapshot.delayedHidingDateString,
                        timeHours = announcementSnapshot.delayedHidingTimeHours,
                        timeMinutes = announcementSnapshot.delayedHidingTimeMinutes,
                        timeString = announcementSnapshot.delayedHidingTimeString,
                    )
                }
            }

            // Опрос
            val attachedSurvey = announcementSnapshot.attachedSurvey
            if (attachedSurvey != null) {
                AttachedSurveySection(attachedSurvey)
            } else {
                AttachSurveySection(announcementSnapshot.newSurvey)
            }

            // Аудитория
            DisplayAudienceHierarchySection(announcementSnapshot.audienceRoots)
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(all = 8.dp)
    }
}