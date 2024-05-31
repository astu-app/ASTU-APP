package org.astu.feature.bulletinBoard.viewModels.userGroups.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.UserGroupModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.CreateUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupCreateContentErrors
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsModelMappers.toModel
import org.astu.feature.bulletinBoard.views.entities.userGroups.creation.CreateUserGroupContent

class CreateUserGroupViewModel : StateScreenModel<CreateUserGroupViewModel.State>(State.CreateContentLoading) {
    sealed class State {
        data object CreateContentLoading : State()
        data object CreateContentLoadingDone : State()
        data object CreateContentLoadingError : State()
        data object NewUserGroupUploading : State()
        data object NewUserGroupUploadingDone : State()
        data object NewUserGroupUploadingError : State()
    }

    private val userGroupModel = UserGroupModel()
    val content: MutableState<CreateUserGroupContent?> = mutableStateOf(null)

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        loadCreateUserGroupContent()
    }

    fun loadCreateUserGroupContent() {
        screenModelScope.launch {
            try {
                mutableState.value = State.CreateContentLoading

                val createContent = userGroupModel.getCreateContent()
                if (!createContent.isContentValid) {
                    constructLoadingCreateUserGroupContentErrorDialogContent(createContent.error)
                    mutableState.value = State.CreateContentLoadingError
                    return@launch
                } else {
                    content.value = CreateUserGroupContent(createContent.content)
                    mutableState.value = State.CreateContentLoadingDone
                }

            } catch (e: Exception) {
                constructLoadingCreateUserGroupContentErrorDialogContent()
                mutableState.value = State.CreateContentLoadingError
                return@launch
            }
        }
    }

    fun canCreate(): Boolean {
        val contentSnapshot = content.value ?: return false
        return contentSnapshot.name.value.isNotBlank()
    }

    fun create() {
        val contentSnapshot = content.value ?: return

        screenModelScope.launch  {
            try {
                mutableState.value = State.NewUserGroupUploading

                val error = userGroupModel.create(contentSnapshot.toModel())
                if (error != null) {
                    constructNewUserGroupUploadingErrorDialogContent(error)
                    mutableState.value = State.NewUserGroupUploadingError
                    return@launch
                } else {
                    mutableState.value = State.NewUserGroupUploadingDone
                }

            } catch  (e: Exception)  {
                constructNewUserGroupUploadingErrorDialogContent()
                mutableState.value = State.NewUserGroupUploadingError
                return@launch
            }
        }
    }

    fun resetAdmin() {
        content.value?.admin?.value = null
    }



    private fun constructLoadingCreateUserGroupContentErrorDialogContent(error: GetUsergroupCreateContentErrors? = null) {
        errorDialogBody = error.humanize()

        onErrorDialogTryAgainRequest = {
            loadCreateUserGroupContent()
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }

    private fun constructNewUserGroupUploadingErrorDialogContent(error: CreateUserGroupErrors? = null) {
        errorDialogBody = error.humanize()

        onErrorDialogTryAgainRequest = {
            create()
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }
}

