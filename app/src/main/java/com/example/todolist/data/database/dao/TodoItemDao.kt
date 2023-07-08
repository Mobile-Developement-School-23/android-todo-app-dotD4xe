package com.example.todolist.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todolist.data.database.entity.TodoItemEntity

/**
 * Data Access Object (DAO) for accessing and manipulating [TodoItemEntity] in the database.
 */
@Dao
interface TodoItemDao {
    @Query("SELECT * FROM todo_items")
    suspend fun getTodoItems(): List<TodoItemEntity>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getTodoItemById(id: String): TodoItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(todoItem: TodoItemEntity)

    @Update suspend fun updateTodoItem(todoItem: TodoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItems(todoItems: List<TodoItemEntity>)

    @Delete suspend fun deleteTodoItem(todoItem: TodoItemEntity)

    @Query("DELETE FROM todo_items")
    suspend fun deleteAllTodoItems()

    /**
     * Replaces all existing [TodoItemEntity] in the database with the given list of [TodoItemEntity].
     * This operation first deletes all existing items and then inserts the new items.
     * @param todoItems The list of [TodoItemEntity] to be replaced.
     */
    @Transaction
    suspend fun replaceTodoItems(todoItems: List<TodoItemEntity>) {
        deleteAllTodoItems()
        insertTodoItems(todoItems)
    }
}
