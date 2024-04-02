package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

class BulletInBoardViewModel : StateScreenModel<BulletInBoardViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    lateinit var content: List<AnnouncementSummaryContent>


    init {
        mutableState.value = State.Loading
        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        content = model.getAnnouncementList()
        mutableState.value = State.LoadingDone
    }
}