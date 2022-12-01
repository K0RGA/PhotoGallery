package com.example.photogallery.utils

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("NotificationReceiver", "received broadcast:${intent?.action}")
        if (resultCode != Activity.RESULT_OK) {

        }

        val requestCode = intent?.getIntExtra(PollWorker.REQUEST_CODE, 0)
        val notification: Notification =
            intent?.getParcelableExtra(PollWorker.NOTIFICATION) ?: Notification()

        val notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager.notify(requestCode ?: 0, notification)
    }
}