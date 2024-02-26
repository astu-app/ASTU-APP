import androidx.compose.ui.window.ComposeUIViewController
import org.astu.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
