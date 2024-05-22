package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupSummaryContent

@Composable
fun DisplayUserGroupListSection(
    userGroups: List<UserGroupSummaryContent>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(all = 16.dp)
        .wrapContentHeight()
) {
    TODO("Доделать список групп пользователей")
//    val presentations = remember { userGroups.map { makeStaticUserGroupText(it.name, it.adminName) }
//
//    Column(modifier = modifier) {
//        presentations.forEach { makeStaticUserGroupText(it) }
//    }
}