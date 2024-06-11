package org.astu.app.notifications

actual fun showNotification(notificationId: Long, title: String, message: String) {
    console.log("Attempting to show notification: $title - $message")
    println("Attempting to show notification: $title - $message")

    if (js("Notification.permission") as String == "granted") {
        console.log("Notification permission granted.")
        js("new Notification(title, { body: message, icon: 'path/to/icon.png' })")
    } else {
        console.log("Requesting notification permission...")
        js("Notification.requestPermission").invoke { permission: String ->
            console.log("Notification permission result: $permission")
            if (permission == "granted") {
                console.log("Notification permission granted after request.")
                js("new Notification(title, { body: message, icon: 'path/to/icon.png' })")
            } else {
                console.log("Notification permission denied.")
            }
        }
    }
}

//actual fun showNotification(notificationId: Long, title: String, message: String) {
//    if (js("Notification.permission") as String == "granted") {
//        js("new Notification(title, { body: message, icon: 'path/to/icon.png' })")
//    } else {
//        js("Notification.requestPermission").invoke { permission: String ->
//            if (permission == "granted") {
//                js("new Notification(title, { body: message, icon: 'path/to/icon.png' })")
//            }
//        }
//    }
//}