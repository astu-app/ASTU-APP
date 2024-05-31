package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.infrastructure.components.CheckboxRow
import kotlin.jvm.JvmName

object UserViewMappers {
    @JvmName("UserSummaryCollectionToStaticViews")
    fun Collection<UserSummary>.toStaticViews(): List<@Composable () -> Unit> =
        this.map { it.toStaticView()}

    @JvmName("UserSummaryToStaticView")
    fun UserSummary.toStaticView(modifier: Modifier = Modifier): @Composable () -> Unit = {
        val user = this
        Column(modifier = modifier) {
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

    /**
     * Преобразование UserSummary в Composable функцию, содержащую чекбокс напротив информации о пользователе.
     */
    @JvmName("UserSummaryToSelectableView")
    fun UserSummary.toSelectableView(
        isChecked: MutableState<Boolean>,
        modifier: Modifier = Modifier.fillMaxWidth(),
        onCheckedStateChanged: (Boolean) -> Unit,
    ): @Composable () -> Unit {
        val user = this
        return {
            CheckboxRow(
                title = user.toStaticView(),
                state = isChecked,
                onCheckedStateChange = onCheckedStateChanged,
                modifier = modifier
            )
        }
    }

    /**
     * Преобразование UserSummary в Composable функцию, содержащую чекбокс напротив информации о пользователе.
     */
    @JvmName("UserSummaryToClickableView")
    fun UserSummary.toClickableView(onClick: () -> Unit): @Composable () -> Unit {
        return this.toStaticView(modifier = Modifier.clickable(onClick = onClick))
    }
}