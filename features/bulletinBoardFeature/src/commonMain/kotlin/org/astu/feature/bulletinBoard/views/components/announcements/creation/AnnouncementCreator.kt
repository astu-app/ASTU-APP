package org.astu.feature.bulletinBoard.views.components.announcements.creation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.viewModels.announcements.actions.CreateAnnouncementViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.AttachSurveySection
import org.astu.feature.bulletinBoard.views.components.announcements.common.DelayedMomentPicker
import org.astu.feature.bulletinBoard.views.components.announcements.common.DisplayAudienceHierarchySection
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent
import org.astu.infrastructure.components.dropdown.DropDown
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
        val announcementSnapshot = announcement.value ?: return

        if (announcementSnapshot.selectedRootId.value == null) {
            Text(
                text = "Вы не можете установить аудиторию объявления, а значит, и создать его",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            return
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier
        ) {
            // Группа пользователей
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Группа пользователей",
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                )
                DropDownSelector(
                    announcementSnapshot.audienceRootsForUserGroupSelection.values,
                    announcementSnapshot.audienceRootsForUserGroupSelection[announcementSnapshot.selectedRootId.value]!!,
                    announcementSnapshot.isSelectUserGroupExpanded
                )
            }

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


            // Опрос
            AttachSurveySection(announcementSnapshot.survey)

            // Аудитория
            if (announcementSnapshot.selectedRootId.value != null)
                DisplayAudienceHierarchySection(listOf(announcementSnapshot.audienceRoots[announcementSnapshot.selectedRootId.value]!!))
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


@Composable
fun DropDownSelector(
    elements: Collection<@Composable () -> Unit>,
    selectedElement: @Composable () -> Unit,
    isExpanded: MutableState<Boolean> = mutableStateOf(false)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        DropDown(elements, isExpanded = isExpanded) {
            selectedElement.invoke()
        }
    }
}