package org.astu.app

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.astu.app.notifications.NotificationService
import org.astu.infrastructure.utils.file.FileUtils

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

const val NOTIFICATION_CHANNEL_ID = "GENERAL_NOTIFICATION_CHANNEL"

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        globalAppContext = this

        super.onCreate(savedInstanceState)
        FileUtils.init(this)
        setContent {
            App()
        }

        // Запуск сервиса уведомлений
        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)
    }
}

lateinit var globalAppContext: Context