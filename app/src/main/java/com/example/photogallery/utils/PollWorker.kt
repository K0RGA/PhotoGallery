package com.example.photogallery.utils

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photogallery.R
import com.example.photogallery.api.FlickrFetchr
import com.example.photogallery.model.GalleryItem
import com.example.photogallery.utils.PhotoGalleryApplication.Companion.NOTIFICATION_CHANNEL_ID

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)
        val items: List<GalleryItem> = if (query.isEmpty()) {
            FlickrFetchr().fetchPhotosRequest()
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } else {
            FlickrFetchr().searchPhotosRequest(query)
                .execute()
                .body()
                ?.photos
                ?.galleryItems
        } ?: emptyList()

        if (items.isEmpty()) {
            return Result.success()
        }

        val resultId = items.first().id
        Log.d("Pollworker", "init")

        if (resultId == lastResultId) {
            Log.d("Pollworker", "Got an old result: $resultId")
        } else {
            Log.d("Pollworker", "Got a new result: $resultId")
            QueryPreferences.setLastResultId(context, resultId)
        }

        val intent = PhotoGalleryApplication.newIntent(context)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val resources = context.resources
        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.new_pictures_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.new_pictures_title))
            .setContentText(resources.getString(R.string.new_pictures_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)

        return Result.success()
    }
}