package org.astu.app.view_models.bulletInBoard

import cafe.adriel.voyager.core.model.StateScreenModel
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.models.bulletInBoard.AnnouncementModel

class CreateAnnouncementViewModel : StateScreenModel<CreateAnnouncementViewModel.State>(State.Init) {
    sealed class State {
        data object Init : State()
        data object Uploading : State()
        data object UploadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    val content: CreateAnnouncementContent = model.getCreateContent()


    init {
        mutableState.value = State.Init
    }

    fun canCreate(): Boolean {
        return model.canCreate(content)
    }

    fun create() {
        mutableState.value = State.Uploading
        model.create(content)
        mutableState.value = State.UploadingDone
    }
}