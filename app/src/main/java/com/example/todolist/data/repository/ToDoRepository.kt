package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface ToDoRepository {
    suspend fun addItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
    suspend fun saveItem(item: TodoItem)
    suspend fun getItems(): StateFlow<RepositoryState>
    suspend fun syncData()
}