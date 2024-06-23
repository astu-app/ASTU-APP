package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class UpdateUsergroupErrors {
    // 400
    /** Имя не содержит символов или содержит исключительно пробельные */
    NameIsNullOrWhitespace,
    // 403
    /** Пользователь не имеет права редактировать группу пользователей */
    UpdateUsergroupForbidden,
    /** Пользователь не имеет права изменять администратора */
    ChangeAdminForbidden,
    /** Пользователь не имеет права изменять состав пользователей */
    ChangeUsersForbidden,
    /** Пользователь не имеет права изменить права пользователей  */
    ChangeUserRightsForbidden,
    // 404
    /** В качестве одного или нескольких id пользователей прикреплен несуществующий в базе id */
    UsersDoNotExist,
//    /** В качестве одного или нескольких id групп пользователей прикреплен несуществующий в базе id */ // remove
//    UserGroupsDoNotExist,
    // 409
    /** Идентификатор администратора передан в массиве идентификаторов участников */
    AdminCannotBeOrdinaryMember;
}