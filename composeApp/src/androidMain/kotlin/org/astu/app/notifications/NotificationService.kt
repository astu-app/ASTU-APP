package org.astu.app.notifications

import android.app.NotificationChannel
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.astu.app.NOTIFICATION_CHANNEL_ID

class NotificationService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        Logger.d("onCreate", tag = "notifications")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            NotificationManager.loadExistingNotifications()
            NotificationManager.connect()
        }

        Logger.d("onStartCommand", tag = "notifications")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationManager.disconnect()
        scope.cancel()

        Logger.d("onDestroy", tag = "notifications")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun createNotificationChannel() {
        val name = "Уведомления"
        val descriptionText = "Уведомления приложения"
        val importance = android.app.NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: android.app.NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}