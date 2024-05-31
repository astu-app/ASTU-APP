package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses

enum class CreateUserGroupErrors {
    // 400
    /** Название группы пользователей null или состоит исключительно из пробельных символов */
    NameIsNullOrWhitespace,
    // 403
    /** Пользователь не имеет права создать группу пользователей */
    UsergroupCreationForbidden,
    // 404
    /** В качестве одного или нескольких id пользователей прикреплен несуществующий в базе id */
    UsersDoNotExist,
    /** В качестве одного или нескольких id групп пользователей прикреплен несуществующий в базе id */
    UserGroupsDoNotExist,
    // 409
    /** Идентификатор администратора передан в массиве идентификаторов участников */
    AdminCannotBeOrdinaryMember,
    /** Группа пользователей не может быть создана, так как порождает цикл на графе групп пользователей */
    CyclicDependency;
}