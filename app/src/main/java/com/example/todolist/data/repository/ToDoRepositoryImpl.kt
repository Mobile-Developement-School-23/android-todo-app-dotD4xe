package com.example.todolist.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.domain.entity.toDtoList
import com.example.todolist.domain.entity.toEntity
import com.example.todolist.domain.entity.toToDoItemDto
import com.example.todolist.data.database.AppDatabase
import com.example.todolist.data.database.entity.toTodoItemList
import com.example.todolist.data.network.api.TodoApiService
import com.example.todolist.data.network.model.AddTodoRequest
import com.example.todolist.data.network.model.AddTodoRequestList
import com.example.todolist.data.network.model.toTodoItemEntityList
import com.example.todolist.data.network.model.toTodoItemList
import com.example.todolist.domain.repository.ToDoRepository
import com.example.todolist.presentation.util.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

/**
 * The repository is part of the data layer.
 * It contains the logic of retrieving and updating ToDo.
 * Scope of application: lives as long as the application itself. Not recreated during setup
 * @property todoApiService [TodoApiService]
 * @property database [AppDatabase]
 */

class ToDoRepositoryImpl @Inject constructor(
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
        } catch (_: Exception) {
            getItems()
        }
    }


    override suspend fun addItem(item: TodoItem) {
        try {
            getItems()
            val revision = dataStoreManager.readRevision()
            database.todoItemDao().insertTodoItem(item.toEntity())
            val response =
                todoApiService.addTodoItem(revision, AddTodoRequest(item.toToDoItemDto()))
            dataStoreManager.writeRevision(response.revision)
            getItems()
        } catch (_: Exception) {
            getItems()
        }
    }

    override suspend fun saveItem(item: TodoItem) {
        try {
            getItems()
            val revision = dataStoreManager.readRevision()
            database.todoItemDao().updateTodoItem(item.toEntity())
            val response = todoApiService.updateTodoItem(
                item.id,
                revision,
                AddTodoRequest(item.toToDoItemDto())
            )
            dataStoreManager.writeRevision(response.revision)
            getItems()
        } catch (_: Exception) {
            getItems()
        }
    }

    override suspend fun deleteItem(item: TodoItem) {
        try {
            getItems()
            val revision = dataStoreManager.readRevision()
            database.todoItemDao().deleteTodoItem(item.toEntity())
            val response = todoApiService.deleteTodoItem(item.id, revision)
            dataStoreManager.writeRevision(response.revision)
            getItems()
        } catch (_: Exception) {
            getItems()
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
                database.todoItemDao().replaceTodoItems(list.toTodoItemEntityList())
            }
            dataStoreManager.writeRevision(response.revision)
        } catch (_: Exception) {
            val cache = database.todoItemDao().getTodoItems().toTodoItemList().reversed()
            todoItems.getAndUpdate {
                RepositoryState(
                    listItems = cache,
                    "No connectoin"
                )
            }
        }
        return todoItems
    }
}









