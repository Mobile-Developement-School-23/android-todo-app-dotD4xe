package com.example.todolist.di

import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.repository.ToDoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(): ToDoRepository{
        return ToDoRepositoryImpl()
    }
}