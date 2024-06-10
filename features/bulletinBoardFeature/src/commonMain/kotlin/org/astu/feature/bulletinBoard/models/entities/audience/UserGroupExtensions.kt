package org.astu.feature.bulletinBoard.models.entities.audience

fun UserGroup.getUserGroupHierarchyMembers() : List<User> =
    flattenUserGroupHierarchy().flatMap { it.members }

fun UserGroup.flattenUserGroupHierarchy(): List<UserGroup> =
    getDescendants().distinctBy { it.id }.toList()

private fun UserGroup?.getDescendants(): Sequence<UserGroup> = sequence {
    if (this@getDescendants == null) return@sequence

    yield(this@getDescendants)

    for (child in this@getDescendants.userGroups) {
        yieldAll(child.getDescendants())
    }
}
