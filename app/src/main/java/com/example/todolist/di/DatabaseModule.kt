package com.example.todolist.di

import android.content.Context
import androidx.room.Room
import com.example.todolist.data.database.AppDatabase
import com.example.todolist.data.database.dao.TodoItemDao
import dagger.Module
import dagger.Provides

@Module
interface DatabaseModule {
    companion object {
        @Provides
        @ApplicationScope
        fun provideAppDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        @Provides
        @ApplicationScope
        fun provideTodoItemDao(appDatabase: AppDatabase): TodoItemDao {
            return appDatabase.todoItemDao()
        }
    }
}
