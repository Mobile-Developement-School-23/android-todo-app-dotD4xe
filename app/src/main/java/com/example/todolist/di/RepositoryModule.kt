package com.example.todolist.di

import androidx.datastore.core.DataStore
import com.example.todolist.domain.repository.ToDoRepository
import com.example.todolist.data.repository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.data.database.AppDatabase
import com.example.todolist.data.network.api.TodoApiService

@Module
interface RepositoryModule {
    companion object {
        @Provides
        @ApplicationScope
        fun provideNoteRepository(
            dataStore: DataStore<Preferences>,
            todoApiService: TodoApiService,
            appDatabase: AppDatabase
        ): ToDoRepository {
            return ToDoRepositoryImpl(dataStore, todoApiService, appDatabase)
        }
    }
}
