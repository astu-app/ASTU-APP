package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.MutableState

/**
 * Интерфейс узла графа, который может быть выбоан
 */
interface ISelectableNode : INode {

    /**
     * Cостояние выбора текущего узла - true, если все дочерние узлы имеют состояние свойства isSelected = true, иначе - false
     */
    val isSelected: MutableState<Boolean>

    /**
     * Установить состояние выбора текущего узла. Также устанавливает такое же состояние у всех дочерних узлов
     */
    fun setSelectionState(state: Boolean)

    /**
     * Метод вызывается при изменении одним из потомков состояния выбора. Если все потомки выбраны, метод устанавливает
     * состояние выбора текущего узла, иначе - сбрасывает
     */
    fun receiveNotificationChildSelectionChanges()
}