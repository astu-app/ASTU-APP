package org.astu.feature.bulletinBoard.viewModels.announcements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.DeleteAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetDelayedHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

class DelayedHiddenAnnouncementsViewModel(private val onReturn: () -> Unit) : StateScreenModel<DelayedHiddenAnnouncementsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingAnnouncementsError : State()
        data object DeletingAnnouncement : State()
        data object DeletingAnnouncementError : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    var content: SnapshotStateList<AnnouncementSummaryContent> = mutableStateListOf()

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Непредвиденная ошибка при загрузке списка объявлений, ожидающих отложенное сокрытие. Повторите попытку"
    val errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        screenModelScope.launch {
            try {
                mutableState.value = State.Loading

                val announcements = model.getDelayedHiddenAnnouncementList()
                if (announcements.isContentValid) {
                    content.removeAll { true }
                    content.addAll(announcements.content!!)
                    mutableState.value = State.LoadingDone
                    return@launch
                } else {
                    constructAnnouncementsLoadingErrorDialogContent(announcements.error)
                    mutableState.value = State.LoadingAnnouncementsError
                }

            } catch (e: Exception) {
                constructAnnouncementsLoadingErrorDialogContent()
                mutableState.value = State.LoadingAnnouncementsError
            }
        }
    }

    fun delete(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.DeletingAnnouncement

                val error = model.delete(id)
                if (error != null) {
                    constructAnnouncementDeletingErrorDialogContent(id, error)
                    mutableState.value = State.DeletingAnnouncementError

                } else {
                    mutableState.value = State.LoadingDone
                    loadAnnouncements()
                    return@launch
                }

            } catch (e: Exception) {
                constructAnnouncementDeletingErrorDialogContent(id)
                mutableState.value = State.DeletingAnnouncementError
            }
        }
    }



    private fun constructAnnouncementsLoadingErrorDialogContent(error: GetDelayedHiddenAnnouncementListErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            loadAnnouncements()
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.Loading
            onReturn.invoke()
        }
    }

    private fun constructAnnouncementDeletingErrorDialogContent(announcementId: Uuid, error: DeleteAnnouncementErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            delete(announcementId)
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }
}