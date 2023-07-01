package com.example.todolist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.model.toDtoList
import com.example.todolist.data.model.toEntity
import com.example.todolist.data.model.toToDoItemDto
import com.example.todolist.database.AppDatabase
import com.example.todolist.database.entity.toTodoItemList
import com.example.todolist.network.api.TodoApiService
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.network.model.AddTodoRequestList
import com.example.todolist.network.model.toTodoItemEntityList
import com.example.todolist.network.model.toTodoItemList
import com.example.todolist.util.DataStoreManager
import com.example.todolist.util.RetryHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.UnknownHostException

class ToDoRepositoryImpl(
    dataStore: DataStore<Preferences>,
    private val todoApiService: TodoApiService,
    private val database: AppDatabase
) : ToDoRepository {

    private val dataStoreManager = DataStoreManager(dataStore)
    private val todoItems = MutableStateFlow(RepositoryState.empty)

    override suspend fun syncData() {
        try {
            val revision = dataStoreManager.readRevision()
            val dbItemList = database.todoItemDao().getTodoItems().toTodoItemList()
            val response = todoApiService.updateTodoList(
                revision,
                AddTodoRequestList(dbItemList.toDtoList())
            )
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            getItems()
        }
    }


    override suspend fun addItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            database.todoItemDao().insertTodoItem(item.toEntity())
            val response =
                todoApiService.addTodoItem(revision, AddTodoRequest(item.toToDoItemDto()))
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            if (e is HttpException) {
                when(e.code()) {
                    400 -> {
                        getItems()
                        addItem(item)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    RetryHandler().retryFunction { todoApiService.addTodoItem(revision, AddTodoRequest(item.toToDoItemDto())) }
                }
            }
            getItems()
        }
    }

    override suspend fun saveItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            database.todoItemDao().updateTodoItem(item.toEntity())
            val response = todoApiService.updateTodoItem(
                item.id,
                revision,
                AddTodoRequest(item.toToDoItemDto())
            )
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            if (e is HttpException) {
                when(e.code()) {
                    400 -> {
                        getItems()
                        saveItem(item)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    RetryHandler().retryFunction {
                        todoApiService.updateTodoItem(
                            item.id,
                            revision,
                            AddTodoRequest(item.toToDoItemDto())
                        )
                    }
                }
            }
            getItems()
        }
    }

    override suspend fun deleteItem(item: TodoItem) {
        val revision = dataStoreManager.readRevision()
        try {
            database.todoItemDao().deleteTodoItem(item.toEntity())
            val response = todoApiService.deleteTodoItem(item.id, revision)
            dataStoreManager.writeRevision(response.revision)
        } catch (e: Exception) {
            getItems()
            CoroutineScope(Dispatchers.IO).launch {
                RetryHandler().retryFunction { todoApiService.deleteTodoItem(item.id, revision) }
            }
        }
    }

    override suspend fun getItems(): StateFlow<RepositoryState> {
        try {
            val response = todoApiService.getTodoList()
            response.list?.let { list ->
                todoItems.getAndUpdate {
                    RepositoryState(
                        listItems = list.toTodoItemList().toMutableList().asReversed(),
                        ""
                    )
                }
                database.todoItemDao().insertTodoItems(list.toTodoItemEntityList())
            }
            dataStoreManager.writeRevision(response.revision)
        } catch (e: UnknownHostException) {
            val cache = database.todoItemDao().getTodoItems().toTodoItemList().reversed()
            todoItems.getAndUpdate {
                RepositoryState(
                    listItems = cache,
                    "No connectoin"
                )
            }
        } catch (e: Exception) {
            val cache = database.todoItemDao().getTodoItems().toTodoItemList().reversed()
            todoItems.getAndUpdate {
                RepositoryState(
                    listItems = cache,
                    e.toString()
                )
            }
        }
        return todoItems
    }
}









