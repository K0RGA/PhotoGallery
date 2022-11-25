package com.example.photogallery.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PollWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {
    override fun doWork(): Result {
        return Result.success()
    }
}