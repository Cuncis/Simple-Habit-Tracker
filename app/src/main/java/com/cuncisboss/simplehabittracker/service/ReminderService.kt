package com.cuncisboss.simplehabittracker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.cuncisboss.simplehabittracker.MainActivity
import com.cuncisboss.simplehabittracker.R
import com.cuncisboss.simplehabittracker.util.Constants
import com.cuncisboss.simplehabittracker.util.Constants.ACTION_SERVICE
import com.cuncisboss.simplehabittracker.util.Constants.NOTIFICATION_CHANNEL_ID
import com.cuncisboss.simplehabittracker.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.cuncisboss.simplehabittracker.util.Constants.NOTIFICATION_ID
import org.koin.core.KoinComponent

class ReminderService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_SERVICE -> {
                    startForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, createNotificationBuilder().build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotificationBuilder() = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_baseline_access_time)
        .setContentTitle("Reminder")
        .setContentIntent(createPendingIntent())

    private fun createPendingIntent() = PendingIntent.getActivity(
        applicationContext,
        0,
        Intent(applicationContext, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_TODO_FRAGMENT
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

}