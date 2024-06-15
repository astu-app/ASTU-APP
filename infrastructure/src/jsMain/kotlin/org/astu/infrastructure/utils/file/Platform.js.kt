package org.astu.infrastructure.utils.file

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.w3c.files.File
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PlatformFile(
    val file: File,
) {
    actual val name: String
        get() = file.name
    actual val path: String?
        get() = null

    actual suspend fun readBytes(): ByteArray = suspendCoroutine { continuation ->
        val reader = FileReader()
        reader.onload = { ev ->
            runCatching {
                val buffer = ev.target
                    ?.unsafeCast<FileReader>()
                    ?.result
                    ?.unsafeCast<ArrayBuffer>()
                    ?: throw IllegalStateException("Could not read file")
                val bytes = Uint8Array(buffer)
                val byteArray = ByteArray(bytes.length)
                for (i in 0 until bytes.length) {
                    byteArray[i] = bytes[i]
                }
                continuation.resume(byteArray)
            }.getOrThrow()
        }

        reader.readAsArrayBuffer(file)
    }

}

actual class PlatformDirectory {
    actual val path: String?
        get() = null
}