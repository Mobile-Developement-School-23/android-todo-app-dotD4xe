package com.example.todolist.domain.repository

import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.data.repository.RepositoryState
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining the contract for a ToDoRepositoryImpl.
 */
interface ToDoRepository {
    suspend fun addItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
    suspend fun saveItem(item: TodoItem)
    suspend fun getItems(): StateFlow<RepositoryState>
    suspend fun syncData()
}
