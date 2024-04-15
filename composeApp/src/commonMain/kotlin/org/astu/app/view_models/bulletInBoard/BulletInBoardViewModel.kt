package org.astu.app.view_models.bulletInBoard

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

class BulletInBoardViewModel : StateScreenModel<BulletInBoardViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    var content: SnapshotStateList<AnnouncementSummaryContent> = mutableStateListOf()


    init {
        mutableState.value = State.Loading
        loadAnnouncements()
    }

    private fun loadAnnouncements() {
        screenModelScope.launch {
            content.clear()

            val announcements = model.getAnnouncementList()
            content.addAll(announcements)

            mutableState.value = State.LoadingDone
        }
    }
}