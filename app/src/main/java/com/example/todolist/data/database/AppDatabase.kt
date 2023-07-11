package com.example.todolist.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todolist.data.database.dao.TodoItemDao
import com.example.todolist.data.database.entity.TodoItemEntity

/**
 * The Room database class for the application.
 * @property todoItemDao The Data Access Object (DAO) for accessing [TodoItemEntity] in the database.
 */
@Database(entities = [TodoItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoItemDao(): TodoItemDao
}
