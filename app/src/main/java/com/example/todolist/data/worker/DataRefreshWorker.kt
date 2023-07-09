package com.example.todolist.data.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todolist.data.repository.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class DataRefreshWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val dataRepository: ToDoRepository
) : Worker(appContext, workerParams) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun doWork(): Result {
        return try {
            coroutineScope.launch {
                dataRepository.getItems()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
