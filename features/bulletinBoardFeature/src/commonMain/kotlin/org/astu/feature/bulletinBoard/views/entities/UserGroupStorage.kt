package org.astu.feature.bulletinBoard.views.entities

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.announcements.common.SelectableUserSummary
import org.astu.feature.bulletinBoard.views.entities.UserStorage.makeSelectableUserText
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.ISelectableNode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.SelectableLeaf
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.SelectableNode
import org.astu.infrastructure.components.CheckboxRow

@Deprecated("Class is deprecated and shouldn't be user anymore")
object UserGroupStorage {

    fun makeSelectableAudience(): INode {
        /*
        Структура групп:
        Группа 1
        + -- Группа 2
        |    + -- Анисимова Анна Максимовна
        |    + -- Романова Милана Матвеевна
        + -- Группа 3
             + -- Группа 4
             |    + -- Фадеев Максим Михайлович
             |    + -- Покровская Анастасия Андреевна
             |    + -- Воробьева Софья Дмитриевна
             + -- Группа 5
                  + -- Левин Тимофей Владимирович
        */
        val user1 = SelectableUserSummary(UserStorage.user1)
        val user2 = SelectableUserSummary(UserStorage.user2)
        val user3 = SelectableUserSummary(UserStorage.user3)
        val user4 = SelectableUserSummary(UserStorage.user4)
        val user5 = SelectableUserSummary(UserStorage.user5)
        val user6 = SelectableUserSummary(UserStorage.user6)

        val group2 = SelectableNode(
            content = { },
            nodes = listOf(
                SelectableLeaf(makeSelectableUserText(user1) { user1.isSelected.value = it }, isSelected = user1.isSelected),
                SelectableLeaf(makeSelectableUserText(user2) { user2.isSelected.value = it }, isSelected = user2.isSelected)
            )
        )
        group2.content = makeSelectableUserGroupText("Группа 2", group2) { group2.setSelectionState(it) }

        val group4 = SelectableNode(
            content = { },
            nodes = listOf(
                SelectableLeaf(makeSelectableUserText(user3) { user3.isSelected.value = it }, isSelected = user3.isSelected),
                SelectableLeaf(makeSelectableUserText(user4) { user4.isSelected.value = it }, isSelected = user4.isSelected),
                SelectableLeaf(makeSelectableUserText(user5) { user5.isSelected.value = it }, isSelected = user5.isSelected)
            )
        )
        group4.content = makeSelectableUserGroupText("Группа 4", group4) { group4.setSelectionState(it) }

        val group5 = SelectableNode(
            content = { },
            nodes = listOf(
                SelectableLeaf(makeSelectableUserText(user6) { user6.isSelected.value = it }, isSelected = user6.isSelected)
            )
        )
        group5.content = makeSelectableUserGroupText("Группа 5", group5) { group5.setSelectionState(it) }

        val group3 = SelectableNode(
            content = { },
            nodes = listOf(group4, group5)
        )
        group3.content = makeSelectableUserGroupText("Группа 3", group3) { group3.setSelectionState(it) }

        val group1 = SelectableNode(
            content = { },
            nodes = listOf(group2, group3)
        )
        group1.content = makeSelectableUserGroupText("Группа 1", group1) { group1.setSelectionState(it) }

        return group1
    }

    fun makeSelectableUserGroupText(text: String, userGroup: ISelectableNode, onCheckedStateChanged: (Boolean) -> Unit): @Composable () -> Unit {
        // Нельзя использовать userGroup.content вместо text, так как в момент вызова метода userGroup.content еще не
        // инициализирован
        return {
            CheckboxRow(
                title = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                },
                state = userGroup.isSelected,
                onCheckedStateChange = onCheckedStateChanged
            )
        }
    }
}