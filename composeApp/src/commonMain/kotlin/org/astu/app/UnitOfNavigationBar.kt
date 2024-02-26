package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class UnitOfNavigationBar(val label: @Composable ()-> Unit, val icon: @Composable ()-> Unit, var screen: Screen)