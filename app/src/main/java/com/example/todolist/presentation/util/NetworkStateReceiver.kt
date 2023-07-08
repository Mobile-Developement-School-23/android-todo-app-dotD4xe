package com.example.todolist.presentation.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.todolist.domain.repository.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Network state receiver class that monitors the availability of network connection and triggers data synchronization.
 * @param connectivityManager The ConnectivityManager instance for monitoring network state.
 * @param repository The ToDoRepository instance for syncing data.
 */
class NetworkStateReceiver @Inject constructor (
    private val connectivityManager: ConnectivityManager,
    private val repository: ToDoRepository
) {

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            CoroutineScope(SupervisorJob()).launch {
                repository.syncData()
            }
        }
    }

    fun register() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
