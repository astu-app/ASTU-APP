package org.astu.infrastructure.utils.file

sealed class PickerType {
    data object Image : PickerType()
    data object Video : PickerType()
    data object ImageAndVideo : PickerType()
    data class File(
        val extensions: List<String>? = null
    ) : PickerType()
}