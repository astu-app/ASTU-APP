package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toView
import org.astu.feature.bulletinBoard.views.components.UserViewMappers.toViews
import org.astu.feature.bulletinBoard.views.components.userGroups.UserGroupViewMappers.toView
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupDetailsContent
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupSummaryContent
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun UserGroupDetails(
    details: MutableState<UserGroupDetailsContent?>,
    modifier: Modifier = Modifier,
) {
    val detailsSnapshot = details.value ?: return

    val navigator = LocalNavigator.currentOrThrow

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Администратор
        val adminView = remember { detailsSnapshot.admin.toView() }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Администратор", style = MaterialTheme.typography.titleMedium)
                adminView.invoke()
            }
        }

        // Родительские группы пользователей
        val parentViews = remember { detailsSnapshot.parents.toViews(navigator) }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DropDown(parentViews) { Text("Родительские группы") }
            }
        }

        // Дочерние группы пользователей
        val childrenViews = remember { detailsSnapshot.children.toViews(navigator) }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DropDown(childrenViews) { Text("Дочерние группы") }
            }
        }

        // Участники
        val memberViews = remember { detailsSnapshot.members.toViews() }
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DropDown(memberViews) { Text("Участники") }
            }
        }
    }
}



private fun Collection<UserGroupSummaryContent>.toViews(navigator: Navigator): List<@Composable () -> Unit> =
    this.map {
        it.toView {
            val userGroupDetailsScreen = UserGroupDetailsScreen(it.id, it.name) { navigator.pop() }
            navigator.push(userGroupDetailsScreen)
        }
    }