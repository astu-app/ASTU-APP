package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.userGroups.summary.UserGroupSummaryContent
import org.astu.infrastructure.components.CheckboxRow
import kotlin.jvm.JvmName

object UserGroupViewMappers {
    /**
     * Представление группы пользователей, на которое можно кликнуть
     */
    @JvmName("UserGroupSummaryContentToView")
    fun UserGroupSummaryContent.toClickableView(onClick: () -> Unit): @Composable () -> Unit = {
        val groupName = remember { this.name }
        val adminName = remember { this.adminName }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onClick),
            )
            Text(
                text = adminName ?: "Без администратора",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
        }
    }

    /**
     * Представление группы пользователей с чекбоксом, который можно отметить
     */
    @JvmName("UserGroupSummaryContentToView")
    fun UserGroupSummaryContent.toSelectableView(
        isSelected: MutableState<Boolean>,
        onCheckedStateChanged: (Boolean) -> Unit
    ): @Composable () -> Unit = {
        val groupName = remember { this.name }
        val adminName = remember { this.adminName }

        CheckboxRow(
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                    Text(
                        text = adminName ?: "Без администратора",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                }
            },
            state = isSelected,
            onCheckedStateChange = onCheckedStateChanged
        )
    }
}