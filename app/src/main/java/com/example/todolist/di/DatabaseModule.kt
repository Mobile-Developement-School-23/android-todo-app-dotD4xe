package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.database.AppDatabase
import com.example.todolist.database.dao.TodoItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
        return appDatabase.todoItemDao()
    }
}