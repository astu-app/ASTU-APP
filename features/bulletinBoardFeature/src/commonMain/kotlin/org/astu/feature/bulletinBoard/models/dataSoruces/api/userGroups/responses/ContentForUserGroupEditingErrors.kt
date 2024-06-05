package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class ContentForUserGroupEditingErrors {
    // 403
    /** Пользователь не имеет права запрашивать данные для редактирования групп пользователей */
    GetUsergroupUpdateContentForbidden,
    // 404
    /** В качестве id группы пользователей прикреплен несуществующий в базе id */
    UserGroupDoesNotExist;
}