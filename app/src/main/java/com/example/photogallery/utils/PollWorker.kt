package com.example.photogallery.utils

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photogallery.api.FlickrFetchr
import com.example.photogallery.model.GalleryItem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val query = QueryPreferences.getStoredQuery(context)
        val lastResultId = QueryPreferences.getLastResultId(context)
        var items = mutableListOf<GalleryItem>()
        if (query.isEmpty()) {
            MainScope().async {
                items = FlickrFetchr()
                    .fetchPhotosRequest().await() as MutableList<GalleryItem>
            }
        } else {
            MainScope().async {
                items = FlickrFetchr()
                    .searchPhotosRequest(query).await() as MutableList<GalleryItem>
            }
        }

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

        return Result.success()
    }
}