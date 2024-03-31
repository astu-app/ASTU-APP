package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import com.benasher44.uuid.Uuid
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

class EditAnnouncementViewModel(announcementId: Uuid) : StateScreenModel<EditAnnouncementViewModel.State>(State.Init) {
    sealed class State {
        data object Init : State()
        data object Loading : State()
        data object LoadingDone : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()

    val content: EditAnnouncementContent = model.getEditContent(announcementId)





    init {
        mutableState.value = State.Init
    }


    fun canEdit(): Boolean {
        return model.canEdit(content)
    }
}