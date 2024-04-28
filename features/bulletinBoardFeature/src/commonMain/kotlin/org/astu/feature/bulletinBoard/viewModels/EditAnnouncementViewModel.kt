package org.astu.feature.bulletinBoard.viewModels

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.views.entities.EditAnnouncementContent

class EditAnnouncementViewModel(
    private val announcementId: Uuid
) : StateScreenModel<EditAnnouncementViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    lateinit var content: EditAnnouncementContent


    init {
        mutableState.value = State.Loading
        loadAnnouncement()
    }

    fun canEdit(): Boolean {
        return model.canEdit(content)
    }

    private fun loadAnnouncement() {
        screenModelScope.launch {
            content = model.getEditContent(announcementId)
            mutableState.value = State.LoadingDone
        }
    }
}