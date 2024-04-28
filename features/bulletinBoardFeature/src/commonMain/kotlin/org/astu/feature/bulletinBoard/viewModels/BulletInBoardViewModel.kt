package org.astu.feature.bulletinBoard.viewModels

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

class BulletInBoardViewModel : StateScreenModel<BulletInBoardViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    var content: SnapshotStateList<AnnouncementSummaryContent> = mutableStateListOf()

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Непредвиденная ошибка при загрузке ленты объявлений. Повторите попытку"
    val errorDialogLabel: MutableState<String> = mutableStateOf(unexpectedErrorTitle)
    val errorDialogBody: MutableState<String> = mutableStateOf(unexpectedErrorBody)
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)

    init {
        mutableState.value = State.Loading
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        screenModelScope.launch {
            try {
                content.clear()

                val announcements = model.getAnnouncementList()
                if (announcements.isContentValid) {
                    content.addAll(announcements.content!!)
                } else {
                    constructErrorDialogContent(announcements.error)
                    showErrorDialog.value = true
                }

                mutableState.value = State.LoadingDone
            } catch (e: Exception) {
                constructErrorDialogContent()
                showErrorDialog.value = true
            }
        }
    }



    private fun constructErrorDialogContent(error: GetPostedAnnouncementListErrors? = null) {
        errorDialogBody.value = when (error) {
            GetPostedAnnouncementListErrors.PostedAnnouncementsListAccessForbidden -> "У вас недостаточно прав для просмотра доски объявлений"
            else -> unexpectedErrorBody
        }
    }
}