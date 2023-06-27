package com.example.todolist.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.repository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.database.AppDatabase
import com.example.todolist.database.dao.TodoItemDao
import com.example.todolist.network.api.TodoApiService
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    private val Context.dataStore by preferencesDataStore(name = "my_data_store")

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideNoteRepository(
        dataStore: DataStore<Preferences>,
        todoApiService: TodoApiService,
        appDatabase: AppDatabase
    ): ToDoRepository {
        return ToDoRepositoryImpl(dataStore, todoApiService, appDatabase)
    }
}