import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.DeleteAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.HidePostedAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CloseSurveyErrors
import org.astu.feature.bulletinBoard.models.services.surveys.SurveyService
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.security.IAccountSecurityManager

class BulletInBoardViewModel : StateScreenModel<BulletInBoardViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingAnnouncementsError : State()
        data object StoppingSurvey : State()
        data object StoppingSurveyError : State()
        data object HidingAnnouncement : State()
        data object HidingAnnouncementError : State()
        data object DeletingAnnouncement : State()
        data object DeletingAnnouncementError : State()
    }

    private val securityManager by GlobalDIContext.inject<IAccountSecurityManager>()

    private val announcementModel: AnnouncementModel = AnnouncementModel()
    private val surveyService: SurveyService = SurveyService()
    var content: SnapshotStateList<AnnouncementSummaryContent> = mutableStateListOf()

    var lazyListState: LazyListState = LazyListState()

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {
        screenModelScope.launch {
            mutableState.value = State.Loading

            try {
                val announcements = announcementModel.getAnnouncementList()
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

    fun addViewToAnnouncement(announcementIndex: Int) {
        if (announcementIndex < 0)
            return

        val announcement = content[announcementIndex]
        if (announcement.seen || announcement.author.id.toString() == securityManager.currentUserId)
            return

        screenModelScope.launch {
            announcementModel.addView(announcement.id)
            announcement.seen = true
            announcement.viewed.value += 1
        }
    }

    fun closeSurveys(surveyIds: List<Uuid>) {
        screenModelScope.launch {
            try {
                surveyIds.forEach { surveyId ->
                    val error = surveyService.close(surveyId)
                    if (error == null) {
                        mutableState.value = State.LoadingDone

                        loadAnnouncements()
                        return@launch
                    } else {
                        constructSurveyClosedErrorDialogContent(surveyId, error)
                        mutableState.value = State.StoppingSurveyError
                    }
                }

                mutableState.value = State.LoadingDone
            } catch (e: Exception) {
                constructAnnouncementsLoadingErrorDialogContent()
                mutableState.value = State.StoppingSurveyError
            }
        }
    }

    fun delete(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.DeletingAnnouncement

                val error = announcementModel.delete(id)
                if (error != null) {
                    constructAnnouncementDeletingErrorDialogContent(id, error)
                    mutableState.value = State.DeletingAnnouncementError

                } else {
                    content.removeAll { it.id == id }
                    mutableState.value = State.LoadingDone
//                    loadAnnouncements()
                    return@launch
                }

            } catch (e: Exception) {
                constructAnnouncementDeletingErrorDialogContent(id)
                mutableState.value = State.DeletingAnnouncementError
            }
        }
    }

    fun hide(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.HidingAnnouncement

                val error = announcementModel.hide(id)
                if (error != null) {
                    constructAnnouncementHidingErrorDialogContent(id, error)
                    mutableState.value = State.HidingAnnouncementError

                } else {
                    content.removeAll { it.id == id }
                    mutableState.value = State.LoadingDone
//                    loadAnnouncements()
                    return@launch
                }

            } catch (e: Exception) {
                constructAnnouncementHidingErrorDialogContent(id)
                mutableState.value = State.HidingAnnouncementError
            }
        }
    }



    private fun constructAnnouncementsLoadingErrorDialogContent(error: GetPostedAnnouncementListErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            loadAnnouncements()
            mutableState.value = State.Loading
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }

    private fun constructAnnouncementDeletingErrorDialogContent(announcementId: Uuid, error: DeleteAnnouncementErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            delete(announcementId)
            mutableState.value = State.Loading
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }

    private fun constructAnnouncementHidingErrorDialogContent(announcementId: Uuid, error: HidePostedAnnouncementErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            hide(announcementId)
            mutableState.value = State.Loading
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }

    private fun constructSurveyClosedErrorDialogContent(surveyId: Uuid, error: CloseSurveyErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            closeSurveys(listOf(surveyId))
            mutableState.value = State.Loading
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }
}