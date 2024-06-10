package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class GetUserListErrors {
    // 403
    /** Пользователь не имеет права получать список групп пользователей, которыми управляет */
    GetOwnedUsergroupsForbidden;
}