package com.example.todolist.di

import androidx.datastore.core.DataStore
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.repository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.database.AppDatabase
import com.example.todolist.network.api.TodoApiService
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

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