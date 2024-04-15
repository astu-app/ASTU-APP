package org.astu.app.components.bulletinBoard.common.models

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.CheckboxRow
import org.astu.app.components.bulletinBoard.announcements.common.SelectableUserSummary
import org.astu.app.components.bulletinBoard.common.models.UserStorage.makeSelectableUserText
import org.astu.app.components.bulletinBoard.common.models.UserStorage.makeStaticUserText
import org.astu.app.entities.bulletInBoard.audienceGraph.*

object UserGroupStorage {
    fun makeStaticAudience(): INode {
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
        val user1 = UserStorage.user1
        val user2 = UserStorage.user2
        val user3 = UserStorage.user3
        val user4 = UserStorage.user4
        val user5 = UserStorage.user5
        val user6 = UserStorage.user6

        val group2 = Node(
            content = makeStaticUserGroupText("Группа 2"),
            nodes = listOf(
                Leaf(makeStaticUserText(user1)),
                Leaf(makeStaticUserText(user2))
            )
        )
        val group4 = Node(
            content = makeStaticUserGroupText("Группа 4"),
            nodes = listOf(
                Leaf(makeStaticUserText(user3)),
                Leaf(makeStaticUserText(user4)),
                Leaf(makeStaticUserText(user5))
            )
        )
        val group5 = Node(
            content = makeStaticUserGroupText("Группа 5"),
            nodes = listOf(Leaf(makeStaticUserText(user6)))
        )
        val group3 = Node(
            content = makeStaticUserGroupText("Группа 3"),
            nodes = listOf(group4, group5)
        )
        val group1 = Node(
            content = makeStaticUserGroupText("Группа 1"),
            nodes = listOf(group2, group3)
        )

        return group1
    }

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
                SelectableLeaf(makeSelectableUserText(user1), isSelected = user1.isSelected),
                SelectableLeaf(makeSelectableUserText(user2), isSelected = user2.isSelected)
            )
        )
        group2.content = makeSelectableUserGroupText("Группа 2", group2)

        val group4 = SelectableNode(
            content = { },
            nodes = listOf(
                SelectableLeaf(makeSelectableUserText(user3), isSelected = user3.isSelected),
                SelectableLeaf(makeSelectableUserText(user4), isSelected = user4.isSelected),
                SelectableLeaf(makeSelectableUserText(user5), isSelected = user5.isSelected)
            )
        )
        group4.content = makeSelectableUserGroupText("Группа 4", group4)

        val group5 = SelectableNode(
            content = { },
            nodes = listOf(
                SelectableLeaf(makeSelectableUserText(user6), isSelected = user6.isSelected)
            )
        )
        group5.content = makeSelectableUserGroupText("Группа 5", group5)

        val group3 = SelectableNode(
            content = { },
            nodes = listOf(group4, group5)
        )
        group3.content = makeSelectableUserGroupText("Группа 3", group3)

        val group1 = SelectableNode(
            content = { },
            nodes = listOf(group2, group3)
        )
        group1.content = makeSelectableUserGroupText("Группа 1", group1)

        return group1
    }

    fun makeStaticUserGroupText(text: String): @Composable () -> Unit {
        return {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
        }
    }

    private fun makeSelectableUserGroupText(text: String, userGroup: ISelectableNode): @Composable () -> Unit {
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
            ) {
                userGroup.setSelectionState(it)
            }
        }
    }
}