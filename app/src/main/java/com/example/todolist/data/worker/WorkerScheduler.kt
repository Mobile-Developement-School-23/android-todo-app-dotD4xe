package com.example.todolist.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkerScheduler(private val context: Context) {

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val updateRequest = PeriodicWorkRequestBuilder<DataRefreshWorker>(
        repeatInterval = 8L, TimeUnit.HOURS
    )
        .setConstraints(constraints)
        .build()

    fun scheduleDataUpdateWork() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "dataUpdateWork",
            ExistingPeriodicWorkPolicy.KEEP,
            updateRequest
        )
    }
}