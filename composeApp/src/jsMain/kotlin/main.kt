import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.ktor.util.*
import org.astu.app.App
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    if (PlatformUtils.platform == Platform.Node) {
        js("process.env.NODE_TLS_REJECT_UNAUTHORIZED = \"0\";")
    }

    onWasmReady {
        CanvasBasedWindow("ASTU Client") {
            App()
        }
    }
}
