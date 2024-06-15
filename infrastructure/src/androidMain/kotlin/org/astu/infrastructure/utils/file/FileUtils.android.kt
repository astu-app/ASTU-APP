package org.astu.infrastructure.utils.file

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual object FileUtils {
    private var registry: ActivityResultRegistry? = null
    private var context: WeakReference<Context?> = WeakReference(null)

    fun init(activity: ComponentActivity) {
        context = WeakReference(activity.applicationContext)
        registry = activity.activityResultRegistry
    }

    actual suspend fun saveFile(
        bytes: ByteArray,
        baseName: String,
        extension: String,
        initialDirectory: String?
    ): PlatformFile? = withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            // Throw exception if registry is not initialized
            val registry = registry ?: throw RuntimeException("Не объявлен init файлов в андроиде")

            // It doesn't really matter what the key is, just that it is unique
            val key = UUID.randomUUID().toString()

            // Get context
            val context = FileUtils.context.get()
                ?: throw RuntimeException("Не объявлен init файлов в андроиде")

            // Get MIME type
            val mimeType = getMimeType(extension)

            // Create Launcher
            val contract = ActivityResultContracts.CreateDocument(mimeType)
            val launcher = registry.register(key, contract) { uri ->
                val platformFile = uri?.let {
                    // Write the bytes to the file
                    context.contentResolver.openOutputStream(it)?.use { output ->
                        output.write(bytes)
                    }

                    PlatformFile(it, context)
                }
                continuation.resume(platformFile)
            }

            // Launch
            launcher.launch("$baseName.$extension")
        }
    }

    actual suspend fun <Out> pickFile(
        type: PickerType,
        mode: PickerMode<Out>,
        title: String?,
        initialDirectory: String?
    ): Out? = withContext(Dispatchers.IO) {
        // Throw exception if registry is not initialized
        val registry = registry ?: throw RuntimeException("Не объявлен init файлов в андроиде")

        // It doesn't really matter what the key is, just that it is unique
        val key = UUID.randomUUID().toString()

        // Get context
        val context = FileUtils.context.get()
            ?: throw RuntimeException("Не объявлен init файлов в андроиде")

        val result: PlatformFiles? = suspendCoroutine { continuation ->
            when (type) {
                PickerType.Image,
                PickerType.Video,
                PickerType.ImageAndVideo -> {
                    when (mode) {
                        is PickerMode.Single -> {
                            val contract = ActivityResultContracts.PickVisualMedia()
                            val launcher = registry.register(key, contract) { uri ->
                                val result = uri?.let { listOf(PlatformFile(it, context)) }
                                continuation.resume(result)
                            }

                            val request = when (type) {
                                PickerType.Image -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                PickerType.Video -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                PickerType.ImageAndVideo -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                else -> throw IllegalArgumentException("Unsupported type: $type")
                            }

                            launcher.launch(request)
                        }

                        is PickerMode.Multiple -> {
                            val contract = ActivityResultContracts.PickMultipleVisualMedia()
                            val launcher = registry.register(key, contract) { uri ->
                                val result = uri.map { PlatformFile(it, context) }
                                continuation.resume(result)
                            }

                            val request = when (type) {
                                PickerType.Image -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                PickerType.Video -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                PickerType.ImageAndVideo -> PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                                else -> throw IllegalArgumentException("Unsupported type: $type")
                            }

                            launcher.launch(request)
                        }
                    }
                }

                is PickerType.File -> {
                    when (mode) {
                        is PickerMode.Single -> {
                            val contract = ActivityResultContracts.OpenDocument()
                            val launcher = registry.register(key, contract) { uri ->
                                val result = uri?.let { listOf(PlatformFile(it, context)) }
                                continuation.resume(result)
                            }
                            launcher.launch(getMimeTypes(type.extensions))
                        }

                        is PickerMode.Multiple -> {
                            val contract = ActivityResultContracts.OpenMultipleDocuments()
                            val launcher = registry.register(key, contract) { uris ->
                                val result = uris.map { PlatformFile(it, context) }
                                continuation.resume(result)
                            }
                            launcher.launch(getMimeTypes(type.extensions))
                        }
                    }
                }
            }
        }

        mode.parseResult(result)
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

    private fun getMimeTypes(fileExtensions: List<String>?): Array<String> {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return fileExtensions
            ?.takeIf { it.isNotEmpty() }
            ?.mapNotNull { mimeTypeMap.getMimeTypeFromExtension(it) }
            ?.toTypedArray()
            ?: arrayOf("*/*")
    }

    private fun getMimeType(fileExtension: String): String {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getMimeTypeFromExtension(fileExtension) ?: "*/*"
    }
}