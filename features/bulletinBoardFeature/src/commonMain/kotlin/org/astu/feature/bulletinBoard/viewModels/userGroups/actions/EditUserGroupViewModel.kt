package org.astu.feature.bulletinBoard.viewModels.userGroups.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.UserGroupModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.ContentForUserGroupEditingErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.UpdateUsergroupErrors
import org.astu.feature.bulletinBoard.models.entities.audience.ContentForUserGroupEditing
import org.astu.feature.bulletinBoard.models.entities.audience.UpdateMemberList
import org.astu.feature.bulletinBoard.models.entities.audience.UpdateUserGroup
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsModelMappers.toModels
import org.astu.feature.bulletinBoard.views.entities.userGroups.editing.EditUserGroupContent

class EditUserGroupViewModel(
    private val userGroupId: Uuid,
    private val rootUserGroupId: Uuid,
    val onReturn: () -> Unit,
) : StateScreenModel<EditUserGroupViewModel.State>(State.EditContentLoading) {
    sealed class State {
        data object EditContentLoading : State()
        data object EditingUserGroup : State()
        data object EditContentLoadingError : State()
        data object ChangesUploading : State()
        data object ChangesUploadingDone : State()
        data object ChangesUploadingError : State()
    }

    private val model: UserGroupModel = UserGroupModel()
    private var original: ContentForUserGroupEditing? = null
    var content: MutableState<EditUserGroupContent?> = mutableStateOf(null)

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    val errorDialogLabel: MutableState<String> = mutableStateOf(unexpectedErrorTitle)
    val errorDialogBody: MutableState<String> = mutableStateOf(unexpectedErrorBody)
    val onErrorDialogTryAgain: MutableState<() -> Unit> = mutableStateOf( { } )
    val onErrorDialogDismiss: MutableState<() -> Unit> = mutableStateOf( { } )
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)



    init {
        loadEditContent()
    }

    fun canEdit(): Boolean {
        return model.canUpdate(content.value)
    }

    fun resetAdmin() {
        content.value?.admin?.value = null
    }

    private fun loadEditContent() {
        screenModelScope.launch {
            mutableState.value = State.EditContentLoading

            try {
                val getContent = model.getUpdateContent(userGroupId, rootUserGroupId)
                Logger.d("getContent: ${getContent.isContentValid}, error: ${getContent.error}", tag = "getContent")
                if (getContent.isContentValid) {
                    original = getContent.content
                    content.value = EditUserGroupContent(getContent.content!!) // так как not-null заложен в isContentValid
                    mutableState.value = State.EditingUserGroup
                    return@launch
                } else {
                    setErrorDialogStateForEditContentLoading(getContent.error)
                    mutableState.value = State.EditContentLoadingError
                }

            } catch (exception: Exception) {
                setErrorDialogStateForEditContentLoading()
                mutableState.value = State.EditContentLoadingError
            }
        }
    }

    fun edit() {
        screenModelScope.launch {
            mutableState.value = State.ChangesUploading

            try {
                val updateModel = toModel() ?: return@launch
                val error = model.update(updateModel, rootUserGroupId)
                if (error == null) {
                    mutableState.value = State.ChangesUploadingDone
                    return@launch
                }

                setErrorDialogStateForChangesUploading(error)
                mutableState.value = State.ChangesUploadingError

            } catch (exception: Exception) {
                setErrorDialogStateForChangesUploading()
                mutableState.value = State.ChangesUploadingError
                Logger.e(exception.message ?: "empty message", exception, "UploadUserGroupChanges")
            }
        }
    }

    private fun toModel(): UpdateUserGroup? {
        val contentSnapshot = content.value ?: return null

        val memberWithChangedRights = contentSnapshot.membersWithChangedRights
        return UpdateUserGroup(
            id = userGroupId,
            name = if (contentSnapshot.nameChanged) contentSnapshot.name.value else null,
            adminChanged = contentSnapshot.adminChanged,
            adminId = contentSnapshot.admin.value?.id,
            members = UpdateMemberList(
                idsToRemove = contentSnapshot.memberIdsToRemove.toList() + memberWithChangedRights.map { it.userId },
                newMembers = contentSnapshot.addedMembers.values.toModels() + memberWithChangedRights.toModels(),
            ),
//            childGroups = UpdateIdentifierList( // remove
//                toAdd = contentSnapshot.addedChildUserGroupIds,
//                toRemove = contentSnapshot.deletedChildUserGroupIds,
//            ),
//            parentGroups = UpdateIdentifierList(
//                toAdd = contentSnapshot.addedParentUserGroupIds,
//                toRemove = contentSnapshot.deletedParentUserGroupIds,
//            ),
////            childGroups = contentSnapshot.selectedChildUserGroupIds,
////            parentGroups = contentSnapshot.selectedChildUserGroupIds,
        )
    }

    private fun setErrorDialogStateForEditContentLoading(error: ContentForUserGroupEditingErrors? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            loadEditContent()
        }
        onErrorDialogDismiss.value = {
            onReturn()
            showErrorDialog.value = false
        }
    }

    private fun setErrorDialogStateForChangesUploading(error:  UpdateUsergroupErrors? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            edit()
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.EditingUserGroup
        }
    }
}