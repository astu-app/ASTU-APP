package org.astu.infrastructure.utils.file

expect object FileUtils {

    suspend fun saveFile(
        bytes: ByteArray,
        baseName: String = "file",
        extension: String,
        initialDirectory: String? = null
    ): PlatformFile?

    suspend fun <Out> pickFile(
        type: PickerType = PickerType.File(),
        mode: PickerMode<Out>,
        title: String? = null,
        initialDirectory: String? = null
    ): Out?

    suspend fun pickDirectory(
        title: String? = null,
        initialDirectory: String? = null
    ): PlatformDirectory?

    fun isDirectoryPickerSupported(): Boolean
}


suspend fun FileUtils.pickFile(
    type: PickerType = PickerType.File(),
    title: String? = null,
    initialDirectory: String? = null,
): PlatformFile? {
    return pickFile(
        type = type,
        mode = PickerMode.Single,
        title = title,
        initialDirectory = initialDirectory,
    )
}