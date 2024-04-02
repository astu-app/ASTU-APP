package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import com.benasher44.uuid.Uuid
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

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
        content = model.getEditContent(announcementId)
        mutableState.value = State.LoadingDone
    }
}