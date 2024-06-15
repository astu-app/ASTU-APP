package org.astu.infrastructure.utils.file

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.dom.url.URL
import org.w3c.files.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

actual object FileUtils {
    actual suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        initialDirectory: String?
    ): PlatformFile? {
        val file = File(
            fileBits = bytes.toTypedArray(),
            fileName = "$baseName.$extension",
        )

        val a = document.createElement("a") as HTMLAnchorElement
        a.href = URL.createObjectURL(file)
        a.download = "$baseName.$extension"

        a.click()

        return PlatformFile(file)
    }

    actual suspend fun <Out> pickFile(
        type: PickerType,
        mode: PickerMode<Out>,
        title: String?,
        initialDirectory: String?
    ): Out? = suspendCoroutine { continution ->
        val input = document.createElement("input") as HTMLInputElement
        input.apply {
            this.type = "file"

            when (type) {
                is PickerType.Image -> accept = "image/*"
                is PickerType.Video -> accept = "video/*"
                is PickerType.ImageAndVideo -> accept = "image/*,video/*"
                is PickerType.File -> type.extensions?.let {
                    accept = type.extensions.joinToString(",") { ".$it" }
                }
            }

            multiple = mode is PickerMode.Multiple
        }

        input.onchange = { event ->
            try {
                // Get the selected files
                val files = event.target
                    ?.unsafeCast<HTMLInputElement>()
                    ?.files
                    ?.asList()

                // Return the result
                val result = files?.map { PlatformFile(it) }
                continution.resume(mode.parseResult(result))
            } catch (e: Throwable) {
                continution.resumeWithException(e)
            }
        }

        input.oncancel = {
        }

        // Trigger the file picker
        input.click()
    }

    actual suspend fun pickDirectory(
        title: String?,
        initialDirectory: String?
    ): PlatformDirectory? {
        TODO("Not yet implemented")
    }

    actual fun isDirectoryPickerSupported(): Boolean {
        TODO("Not yet implemented")
    }

}