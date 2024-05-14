package org.astu.feature.bulletinBoard.models.entities.audience

import androidx.compose.ui.util.fastDistinctBy

/**
 * Иерархия групп пользователей
 * @param roots список корней иерархии
 */
data class UserGroupHierarchy(val roots: List<UserGroup>) {
    val allMembers: List<User> = getAllHierarchyMembers()



    private fun getAllHierarchyMembers(): List<User> =
        roots.flatMap { gethierarchyMembers(it) }

    private fun gethierarchyMembers(userGroup: UserGroup): List<User> =
        (userGroup.userGroups.flatMap { gethierarchyMembers(it) } + userGroup.members).fastDistinctBy { it.id }
}