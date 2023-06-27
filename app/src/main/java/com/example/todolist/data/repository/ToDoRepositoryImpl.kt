package com.example.todolist.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.data.model.TodoItem
import com.example.todolist.database.AppDatabase
import com.example.todolist.database.entity.toTodoItemEntityList
import com.example.todolist.database.entity.toTodoItemList
import com.example.todolist.network.api.TodoApiService
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.util.DataStoreManager
import com.example.todolist.util.toToDoItemDto
import com.example.todolist.util.toTodoItemList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map

class ToDoRepositoryImpl(
    dataStore: DataStore<Preferences>,
    private val todoApiService: TodoApiService,
    private val database: AppDatabase
) : ToDoRepository {

    private val dataStoreManager = DataStoreManager(dataStore)

    override suspend fun addItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            val response = todoApiService.addTodoItem(revision, AddTodoRequest(item.toToDoItemDto()))
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            Log.d("ayash", "response1 ${e.message}")
        }
    }

    override suspend fun saveItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            val response = todoApiService.updateTodoItem(item.id, revision, AddTodoRequest(item.toToDoItemDto()))
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            Log.d("ayash", "response2 ${e.message}")
        }
    }

    override suspend fun deleteItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            val response = todoApiService.deleteTodoItem(item.id, revision)
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            Log.d("ayash", "response3 ${e.message}")
        }
    }

    override suspend fun getItems(): Flow<List<TodoItem>> = flow {
        try {
            val response = todoApiService.getTodoList()
            response.list?.let {
                emit(it.toTodoItemList().toMutableList().asReversed())
                database.todoItemDao().insertTodoItems(it.toTodoItemEntityList())
            }
            dataStoreManager.writeRevision(response.revision)
            Log.d("ayash", response.revision.toString())
        } catch (e: Exception) {
            Log.d("ayash", "response4 ${e.message}")
            val cache = database.todoItemDao().getTodoItems().toTodoItemList().reversed()
            emit(cache)
        }
    }
}
