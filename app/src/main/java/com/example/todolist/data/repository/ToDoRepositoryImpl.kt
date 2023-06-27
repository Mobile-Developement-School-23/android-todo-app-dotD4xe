package com.example.todolist.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.data.model.TodoItem
import com.example.todolist.network.api.TodoApiService
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.util.DataStoreManager
import com.example.todolist.util.toToDoItemDto
import com.example.todolist.util.toTodoItemList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate

class ToDoRepositoryImpl(dataStore: DataStore<Preferences>,
                         private val todoApiService: TodoApiService
    ) : ToDoRepository {

    private val todoItems = MutableStateFlow<MutableList<TodoItem>>(mutableListOf())
    private val dataStoreManager = DataStoreManager(dataStore)

    //error
    override suspend fun addItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            Log.d("ayash", "response1 items ${todoItems.value}")
            val response = todoApiService.addTodoItem(revision, AddTodoRequest(item.toToDoItemDto()))
            dataStoreManager.writeRevision(response.revision)
            Log.d("ayash", "response1 items ${todoItems.value}")
        } catch (e: Exception) {
            Log.d("ayash", dataStoreManager.readRevision().toString())
            Log.d("ayash", "response1 ${e.message}")
        }
    }

    //error
    override suspend fun saveItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            val updatedItems = todoItems.value.toMutableList()
            val index = updatedItems.indexOfFirst { it.id == item.id }
            if (index != -1) {
                updatedItems[index] = item
                val response = todoApiService.updateTodoItem(item.id, revision, AddTodoRequest(item.toToDoItemDto()))
                dataStoreManager.writeRevision(response.revision)
                todoItems.getAndUpdate { it.apply { this[index] = item } }
            }
        } catch (e: Exception) {
            Log.d("ayash", "response2 ${e.message}")
        }
    }

    override suspend fun deleteItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            val response = todoApiService.deleteTodoItem(item.id, revision)
            dataStoreManager.writeRevision(response.revision)
            todoItems.getAndUpdate { it.apply{ it.remove(item) } }

        } catch (e: Exception) {
            Log.d("ayash", "response3 ${e.message}")
        }
    }

    override suspend fun getItems(): StateFlow<MutableList<TodoItem>> {
        try {
            val response = todoApiService.getTodoList()

            response.list?.let {
                todoItems.value = it.toTodoItemList().toMutableList().asReversed()
            }
            dataStoreManager.writeRevision(response.revision)
            Log.d("ayash", response.revision.toString())
        } catch (e: Exception) {
            Log.d("ayash", "response4 ${e.message}")
        }
        return todoItems.asStateFlow()
    }
}
