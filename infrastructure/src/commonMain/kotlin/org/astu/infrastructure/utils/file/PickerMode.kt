package org.astu.infrastructure.utils.file

sealed class PickerMode<Out> {
    abstract fun parseResult(value: PlatformFiles?): Out?

    data object Single : PickerMode<PlatformFile>() {
        override fun parseResult(value: PlatformFiles?): PlatformFile? {
            return value?.firstOrNull()
        }
    }

    data object Multiple : PickerMode<PlatformFiles>() {
        override fun parseResult(value: PlatformFiles?): PlatformFiles? {
            return value?.takeIf { it.isNotEmpty() }
        }
    }
}