package com.example.todolist.data.repository

import android.util.Log
import com.example.todolist.data.model.TodoItem
import com.example.todolist.network.TodoApiService
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.network.model.ToDoItem
import com.example.todolist.util.Importance
import com.example.todolist.util.toDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import java.util.Calendar

class ToDoRepositoryImpl : ToDoRepository {

    val calendar: Calendar = Calendar.getInstance()

    var itemId = 12
    private val todoItem = TodoItem("1", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…", Importance.NORMAL, null, false, calendar.time, null)
    private val todoItem2 = todoItem.copy(id = "2", content = "Привет, что да как", importance = Importance.LOW)
    private val todoItem3 = todoItem.copy(id = "3", content = "Доделать лабу по ОБД потому что препод очень душный и сильно валит, если не сделаю мне конец",  importance = Importance.NORMAL, deadline = "24 августа 2022".toDate())
    private val todoItem4 = todoItem.copy(id = "4", content = "Украсть машину", importance = Importance.HIGH, deadline = "16 августа 2022".toDate(), isDone = false)
    private val todoItem5 = todoItem.copy(id = "5", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = true)
    private val todoItem6 = todoItem.copy(id = "6", content = "Я устал", importance = Importance.HIGH, isDone = false)
    private val todoItem7 = todoItem.copy(id = "7", content = "Уехать из России", importance = Importance.NORMAL, deadline = "21 июля 2025".toDate(), isDone = false)
    private val todoItem8 = todoItem.copy(id = "8", content = "Что-то надо срочно делать", importance = Importance.NORMAL, isDone = false)
    private val todoItem9 = todoItem.copy(id = "9", content = "Ку ку", importance = Importance.HIGH, isDone = false)
    private val todoItem10 = todoItem.copy(id = "10", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = false)
    private val todoItem11 = todoItem.copy(id = "11", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = false)

    val todoItems = MutableStateFlow(mutableListOf(todoItem, todoItem2, todoItem3, todoItem4, todoItem5, todoItem6, todoItem7, todoItem8, todoItem9, todoItem10, todoItem11))
// пофиксить на апплай и добавить ди аписервис и разобраться с моделями
//    private val todoItems = MutableStateFlow<MutableList<ToDoItem>>(mutableListOf())
//
//
//    override suspend fun addItem(item: ToDoItem) {
//        val response = apiService.addTodoItem(todoItems.value.size, AddTodoRequest(item))
//        if (response.status == "success") {
//            todoItems.getAndUpdate { it.add(item) }
//        }
//    }
//
//    override suspend fun saveItem(item: TodoItem) {
//        val response = apiService.updateTodoItem(item.id ?: "", todoItems.value.size, AddTodoRequest(item))
//        if (response.status == "success") {
//            val updatedItems = todoItems.value.toMutableList()
//            val index = updatedItems.indexOfFirst { it.id == item.id }
//            if (index != -1) {
//                updatedItems[index] = item
//                todoItems.getAndUpdate { it[index] = item }
//            }
//        }
//    }
//
//    override suspend fun deleteItem(item: TodoItem) {
//        val response = apiService.deleteTodoItem(item.id ?: "", todoItems.value.size)
//        if (response.status == "success") {
//            todoItems.getAndUpdate { it.remove(item) }
//        }
//    }
//
//    override suspend fun getItems(): StateFlow<MutableList<TodoItem>> {
//        val response = apiService.getTodoList()
//        if (response.status == "success") {
//            response.list?.let {
//                todoItems.value = it.toMutableList()
//            }
//        }
//    }


    override suspend fun addItem(item: TodoItem) {
        todoItems.getAndUpdate {
            it.apply { this.add(item) }
        }
    }

    override suspend fun saveItem(item: TodoItem) {
        todoItems.getAndUpdate { currentItems ->
            val updatedItems = currentItems.toMutableList()
            val index = updatedItems.indexOfFirst { it.id == item.id }
            if (index != -1) {
                updatedItems[index] = item
            }
            updatedItems
        }
        Log.d("ayash", "repo ${item}")
        Log.d("ayash", "repo items ${todoItems.value}")
    }

    override suspend fun deleteItem(item: TodoItem) {
        todoItems.getAndUpdate {
            it.apply { this.remove(item) }
        }
    }

    override suspend fun getItems(): StateFlow<MutableList<TodoItem>> {
        return todoItems.asStateFlow()
    }
}
