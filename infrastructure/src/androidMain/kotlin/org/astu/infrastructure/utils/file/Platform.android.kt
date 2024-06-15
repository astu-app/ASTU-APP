package org.astu.infrastructure.utils.file

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual class PlatformFile(
    val uri: Uri,
    private val context: Context,
) {
    actual val name: String by lazy {
        context.getFileName(uri) ?: throw IllegalStateException("Failed to get file name")
    }
    actual val path: String? = uri.path

    actual suspend fun readBytes(): ByteArray = withContext(Dispatchers.IO) {
        context
            .contentResolver
            .openInputStream(uri)
            .use { stream -> stream?.readBytes() }
            ?: throw IllegalStateException("Failed to read file")
    }

    private fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
        else -> uri.path?.let(::File)?.name
    }

    private fun Context.getContentFileName(uri: Uri): String? = runCatching {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                .let(cursor::getString)
        }
    }.getOrNull()
}

actual class PlatformDirectory {
    actual val path: String?
        get() = TODO("Not yet implemented")
}