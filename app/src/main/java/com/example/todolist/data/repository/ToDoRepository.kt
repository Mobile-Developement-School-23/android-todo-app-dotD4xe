package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem
import com.example.todolist.util.Importance
import com.example.todolist.util.toDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Calendar

interface ToDoRepository {
    suspend fun addItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
    suspend fun saveItem(item: TodoItem)
    suspend fun getItems(): StateFlow<model>

    suspend fun updateTodoList()
}