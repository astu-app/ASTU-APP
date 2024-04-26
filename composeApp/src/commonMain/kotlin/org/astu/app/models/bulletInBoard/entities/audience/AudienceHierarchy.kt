package org.astu.app.models.bulletInBoard.entities.audience

/**
 * Иерархия групп пользователей
 * @param roots список корней иерархии
 */
data class AudienceHierarchy(val roots: List<UserGroup>)