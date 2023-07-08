package com.example.todolist.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.data.worker.DataRefreshWorker
import com.example.todolist.data.worker.WorkerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
interface AppModule {
    companion object {

        private val Context.dataStore by preferencesDataStore(name = "my_data_store")
        @Provides
        @ApplicationScope
        fun provideDataStore(context: Context): DataStore<Preferences> {
            return context.dataStore
        }

        @Provides
        @ApplicationScope
        fun provideConnectivityManager(context: Context) =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        @Provides
        @ApplicationScope
        fun provideWorkerFactory(workerProvider: Provider<DataRefreshWorker.Factory>): WorkerFactory {
            return WorkerFactory(workerProvider)
        }

    }
}
