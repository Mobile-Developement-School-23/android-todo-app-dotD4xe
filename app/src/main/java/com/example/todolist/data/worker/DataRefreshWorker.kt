package com.example.todolist.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todolist.domain.repository.ToDoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Worker class responsible for refreshing data in the background using WorkManager.
 * @property appContext The application context.
 * @property workerParams The worker parameters.
 * @property dataRepository The data repository used to fetch the updated data.
 */
class DataRefreshWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val dataRepository: ToDoRepository
) : Worker(appContext, workerParams) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {
        return try {
            coroutineScope.launch {
                dataRepository.syncData()
            }
            Result.success()
        } catch (_: Exception) {
            Result.failure()
        }
    }

    /**
     * Factory interface for creating instances of [DataRefreshWorker].
     */
    @AssistedFactory
    interface Factory {
        fun create(
            context: Context,
            params: WorkerParameters,
        ): DataRefreshWorker
    }
}
