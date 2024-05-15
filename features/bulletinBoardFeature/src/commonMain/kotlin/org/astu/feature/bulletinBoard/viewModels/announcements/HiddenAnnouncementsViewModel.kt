package org.astu.feature.bulletinBoard.viewModels.announcements

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.DeleteAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.RestoreHiddenAnnouncementErrors
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

class HiddenAnnouncementsViewModel(private val onReturn: () -> Unit) : StateScreenModel<HiddenAnnouncementsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingAnnouncementsError : State()
        data object DeletingAnnouncement : State()
        data object DeletingAnnouncementError : State()
        data object RestoringAnnouncement : State()
        data object RestoringAnnouncementError : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    var content: SnapshotStateList<AnnouncementSummaryContent> = mutableStateListOf()

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Непредвиденная ошибка при загрузке списка скрытых объявлений. Повторите попытку"
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

                content.clear()

                val announcements = model.getHiddenAnnouncementList()
                if (announcements.isContentValid) {
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
                    return@launch
                }

            } catch (e: Exception) {
                constructAnnouncementDeletingErrorDialogContent(id)
                mutableState.value = State.DeletingAnnouncementError
            }
        }
    }

    fun restore(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.RestoringAnnouncement

                val error = model.restore(id)
                if (error != null) {
                    constructAnnouncementRestoringErrorDialogContent(id, error)
                    mutableState.value = State.RestoringAnnouncementError

                } else {
                    mutableState.value = State.LoadingDone
                    return@launch
                }

            } catch (e: Exception) {
                constructAnnouncementRestoringErrorDialogContent(id)
                mutableState.value = State.RestoringAnnouncementError
            }
        }
    }



    private fun constructAnnouncementsLoadingErrorDialogContent(error: GetHiddenAnnouncementListErrors? = null) {
        errorDialogBody = when (error) {
            GetHiddenAnnouncementListErrors.HiddenAnnouncementsListAccessForbidden -> "У вас недостаточно прав для просмотра списка скрытых объявлений"
            else -> unexpectedErrorBody
        }
        onErrorDialogTryAgainRequest = {
            loadAnnouncements()
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
            onReturn.invoke()
        }
    }

    private fun constructAnnouncementDeletingErrorDialogContent(announcementId: Uuid, error: DeleteAnnouncementErrors? = null) {
        errorDialogBody = when (error) {
            DeleteAnnouncementErrors.AnnouncementDeletionForbidden -> "У вас недостаточно прав для удаления объявления"
            DeleteAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            else -> unexpectedErrorBody
        }
        onErrorDialogTryAgainRequest = {
            delete(announcementId)
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }

    private fun constructAnnouncementRestoringErrorDialogContent(announcementId: Uuid, error: RestoreHiddenAnnouncementErrors? = null) {
        errorDialogBody = when (error) {
            RestoreHiddenAnnouncementErrors.RestoreForbidden -> "У вас недостаточно прав для восстановления объявления"
            RestoreHiddenAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            RestoreHiddenAnnouncementErrors.AnnouncementNotHidden -> "Нельзя восстановить объявление, которое не было скрыто"
            else -> unexpectedErrorBody
        }
        onErrorDialogTryAgainRequest = {
            restore(announcementId)
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }
}