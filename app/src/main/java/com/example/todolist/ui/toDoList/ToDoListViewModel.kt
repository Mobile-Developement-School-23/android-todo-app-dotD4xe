package com.example.todolist.ui.toDoList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.network.TodoApiClient
import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.network.model.AddTodoRequest1
import com.example.todolist.network.model.ToDoItem
import com.example.todolist.ui.toDoList.model.TodoListState
import com.example.todolist.util.Importance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    private val _todoItems = MutableStateFlow(TodoListState.empty)
    val todoItems = _todoItems.asStateFlow()

    init {
        loadTodoItems()
    }

    fun loadTodoItems() {
        viewModelScope.launch {
            _todoItems.update { previousState ->
                val completedCount: Int
                val shouldShowCompleted = previousState.isDone

                val items = repository.getItems()
                    .value
                    .also { completedCount = it.filter { it.isDone }.size }
                    .filter {
                        if (shouldShowCompleted) {
                            true
                        } else {
                            it.isDone.not()
                        }
                    }

                TodoListState(
                    listItems = items,
                    completed = completedCount,
                    isDone = previousState.isDone
                )
            }

//            try {
//                val list = listOf(
//                    ToDoItem("2", "friendssss", Importance.LOW.toString(), 1687598430, false, null, 1687598430, 1687598430, "dsf"),
//                    ToDoItem("3", "hello", Importance.LOW.toString(), 1687598430, false, null, 1687598430, 1687598430, "dsf"))
//
//                val response = TodoApiClient.todoApiService.updateTodoList(11, AddTodoRequest1(list))
////                AddTodoRequest1(list)
//                Log.d("ayash", "response $response")
//            } catch (e: Exception) {
//                Log.d("ayash", "response ${e.message}")
//            }
        }
    }

    fun addItem(item: TodoItem) {
        viewModelScope.launch {
            repository.addItem(item)
            loadTodoItems()
        }
    }

    fun checkTodoItem(todoItem: TodoItem) {
        val checkedItem = todoItem.copy(isDone = todoItem.isDone.not())
        viewModelScope.launch {
            repository.saveItem(checkedItem)
            loadTodoItems()
        }
    }

    fun changeCompletedTodosVisibility() {
        _todoItems.update {
            TodoListState(
                listItems = it.listItems,
                completed = it.completed,
                isDone = it.isDone.not()
            )
        }
        Log.d("ayash", "clock see ${_todoItems.value.listItems}")
        loadTodoItems()
    }

    fun saveItem(item: TodoItem) {
        viewModelScope.launch {
            repository.saveItem(item)
            loadTodoItems()
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
            loadTodoItems()
        }
    }

}