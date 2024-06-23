package org.astu.feature.bulletinBoard.views.entities.userGroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup

object UserGroupToViewMappers {
    fun UserGroup.toView(onTap: () -> Unit): @Composable () -> Unit {
        val userGroup = this
        return {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onTap)
            ) {
                Text(
                    text = userGroup.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
                Text(
                    text = userGroup.adminName ?: "Без администратора",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                )
            }
        }
    }
}