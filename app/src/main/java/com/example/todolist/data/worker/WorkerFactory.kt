package com.example.todolist.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

/**
 * Factory class responsible for creating workers for WorkManager.
 * @property workerProvider The provider for the DataRefreshWorker.Factory.
 */
class WorkerFactory @Inject constructor(
    private val workerProvider: Provider<DataRefreshWorker.Factory>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {

            DataRefreshWorker::class.java.name -> workerProvider.get().create(appContext, workerParameters)
            else -> null
        }
    }
}
