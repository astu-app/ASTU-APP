package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import com.benasher44.uuid.Uuid
import org.astu.app.entities.bulletInBoard.announcement.details.AnnouncementDetailsContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

class AnnouncementDetailsViewModel (
    private val announcementId: Uuid
) : StateScreenModel<AnnouncementDetailsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    lateinit var content: AnnouncementDetailsContent


    init {
        mutableState.value = State.Loading
        loadDetails()
    }

    private fun loadDetails() {
        content = model.getDetails(announcementId)
        mutableState.value = State.LoadingDone
    }
}