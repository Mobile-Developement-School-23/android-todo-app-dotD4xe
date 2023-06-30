package com.example.todolist.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.model.toDtoList
import com.example.todolist.data.model.toEntity
import com.example.todolist.database.AppDatabase
import com.example.todolist.database.entity.toTodoItemEntityList
import com.example.todolist.database.entity.toTodoItemList
import com.example.todolist.network.api.TodoApiService
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.network.model.AddTodoRequestList
import com.example.todolist.network.model.toEntityList
import com.example.todolist.util.DataStoreManager
import com.example.todolist.util.RequestError
import com.example.todolist.util.toToDoItemDto
import com.example.todolist.util.toTodoItemList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import retrofit2.HttpException
import java.net.UnknownHostException

class ToDoRepositoryImpl(
    dataStore: DataStore<Preferences>,
    private val todoApiService: TodoApiService,
    private val database: AppDatabase
) : ToDoRepository {

    private val dataStoreManager = DataStoreManager(dataStore)
    private val todoItems = MutableStateFlow(model.empty)

    override suspend fun updateTodoList() {
//        try {
//            val revision = dataStoreManager.readRevision()
////            val response = todoApiService.updateTodoList(
////                revision,
////                AddTodoRequestList(itemList.toDtoList())
////            )
//            database.todoItemDao().insertTodoItems(response.list!!.toEntityList())
//            dataStoreManager.writeRevision(response.revision)
//        } catch (e: Exception) {
//            getItems()
//            Log.d("ayash", "response2 ${e.message}")
//        }
    }

    override suspend fun addItem(item: TodoItem) {
        try {
            database.todoItemDao().insertTodoItem(item.toEntity())
            val revision = dataStoreManager.readRevision()
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
            }
            getItems()
            Log.d("ayash", "response1 ${e.message}")
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
            }
            getItems()
            Log.d("ayash", "response2 ${e.message}")
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
            Log.d("ayash", "response3 ${e.message}")
        }
    }

    override suspend fun getItems(): StateFlow<model> {
        try {
            val response = todoApiService.getTodoList()
            response.list?.let { list ->
                todoItems.getAndUpdate {
                    model(
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
                model(
                    listItems = cache,
                    "No connectoin"
                )
            }
        } catch (e: Exception) {
            val cache = database.todoItemDao().getTodoItems().toTodoItemList().reversed()

//            var exp = e.message.toString()
//            if (e is HttpException) {
//                Log.d("ayash", "CODE ${e.code()}")
//                e.response()?.errorBody()?.string().toString()
//                    .let { message ->
//                        when (e.code()) {
//                            400 -> {
//                                if (message.contains("unsynchronized")) {
//                                    exp = "Error synchronized"
//                                } else {
//                                    exp = "Bad request"
//                                }
//                            }
//
//                            500 -> exp = "hz"
//                            404 -> exp = "Item not found"
//                            401 -> exp = "Not authorization"
//                            else -> e
//                        }
//                    }
//
//            }
            todoItems.getAndUpdate {
                model(
                    listItems = cache,
                    e.toString()
                )
            }
        }
        return todoItems
    }
}









