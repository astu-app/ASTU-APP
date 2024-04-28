package org.astu.feature.bulletinBoard.models.entities.audience

/**
 * Иерархия групп пользователей
 * @param roots список корней иерархии
 */
data class AudienceHierarchy(val roots: List<UserGroup>)