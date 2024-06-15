package org.astu.infrastructure.utils.file

expect class PlatformFile {
    val name: String
    val path: String?

    suspend fun readBytes(): ByteArray
}

val PlatformFile.baseName: String
    get() = name.substringBeforeLast(".", name)

val PlatformFile.extension: String
    get() = name.substringAfterLast(".")

expect class PlatformDirectory {
    val path: String?
}

typealias PlatformFiles = List<PlatformFile>

