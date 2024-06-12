package org.astu.infrastructure

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class StateScreenModel<S>(initialState: S) : ScreenModel, JavaSerializable {
    protected val mutableState: MutableState<S> = mutableStateOf(initialState)
    val state: State<S> = mutableState
}