package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class DeleteUserGroupErrors {
    // 403
    /** Пользователь не имеет права удалить группу пользователей */
    UsergroupDeletionForbidden,
    // 404
    /** В качестве id группы пользователей прикреплен несуществующий в базе id */
    UserGroupDoesNotExist;
}