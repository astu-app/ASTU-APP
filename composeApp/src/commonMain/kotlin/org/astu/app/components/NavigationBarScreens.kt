package org.astu.app.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import org.astu.app.UnitOfNavigationBar

@Composable
fun NavigationBarScreens(selected: Int, units: List<UnitOfNavigationBar>, onSelect: (Int) -> Unit) {
    NavigationBar {
        units.forEachIndexed { index, unit ->
            NavigationBarItem(index == selected, icon = unit.icon, label = unit.label, onClick = { onSelect(index) })
        }
    }
}