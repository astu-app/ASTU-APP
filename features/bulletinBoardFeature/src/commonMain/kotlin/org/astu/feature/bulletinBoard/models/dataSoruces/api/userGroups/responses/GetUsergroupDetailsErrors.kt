package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class GetUsergroupDetailsErrors {
    // 403
    /** Пользователь не имеет права запрашивать детали группы пользователей */
    UetUsergroupDetailsForbidden,
    // 404
    /** В качестве id группы пользователей прикреплен несуществующий в базе id */
    UserGroupDoesNotExist;
}