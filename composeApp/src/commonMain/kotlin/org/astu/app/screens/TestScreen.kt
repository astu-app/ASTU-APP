package org.astu.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import cafe.adriel.voyager.core.screen.Screen

class TestScreen : Screen {
    companion object {
        val views: ViewsMap = mutableStateMapOf()

        fun addViews(adds: ViewsMap.() -> Unit) {
            views.adds()
        }
    }

    private var currentView: MutableState<String?> = mutableStateOf(null)

    @Composable
    override fun Content() {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column {
//                        Text(BuildConfig.APP_NAME + " version:" + BuildConfig.APP_VERSION)
                        HorizontalDivider()
                        views.forEach {
                            TextButton({ currentView.value = it.key }) {
                                Text(it.key)
                            }
                        }
                    }
                }
            },
            drawerState = rememberDrawerState(DrawerValue.Open),
            scrimColor = DrawerDefaults.scrimColor
        ) {
            if (currentView.value != null) {
                views[currentView.value]?.invoke()
            }
        }
    }
}

typealias ViewsMap = SnapshotStateMap<String, @Composable () -> Unit>