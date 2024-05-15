package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import kotlin.jvm.JvmName

object UserViewMappers {
    @JvmName("UserSummaryCollectionToViews")
    fun Collection<UserSummary>.toViews(): List<@Composable () -> Unit> =
        this.map { it.toView()}

    @JvmName("UserGroupToView")
    fun UserSummary.toView(): @Composable () -> Unit = {
        val user = this
        Column(modifier = Modifier) {
            Text(
                text = user.firstName
            )
            val secondPartOfName =
                if (user.patronymic != null)
                    "${user.secondName} ${user.patronymic}"
                else user.secondName
            Text(
                text = secondPartOfName,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}