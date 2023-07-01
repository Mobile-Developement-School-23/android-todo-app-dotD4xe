package com.example.todolist.util

import android.util.Log
import kotlinx.coroutines.delay

class RetryHandler {
    private val maxRetries = 3
    private val retryInterval = 1000L

    suspend fun retryFunction(function: suspend () -> Unit) {
        var retries = 0
        while (retries < maxRetries) {
            try {
                Log.d("ayash", "Error occurred")
                return function()
            } catch (e: Exception) {
                Log.d("ayash", "Error occurred: ${e.message}")
            }
            delay(retryInterval)
            retries++
        }
    }
}





