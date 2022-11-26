package com.example.photogallery.utils

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.photogallery.PhotoGalleryActivity
import com.example.photogallery.R

class PhotoGalleryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID,name,importance)
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        fun newIntent(context: Context): Intent{
            return Intent(context,PhotoGalleryActivity::class.java)
        }

        const val NOTIFICATION_CHANNEL_ID = "flickr_poll"
    }
}