package com.example.todolist.presentation.todoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.domain.repository.ToDoRepository
import com.example.todolist.presentation.todoList.model.TodoListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ToDoListViewModel class is responsible for handling the business logic and data flow of the ToDoList screen.
 * @param repository The ToDoRepository instance for data operations.
 */
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
            repository.getItems().collect { items ->
                _todoItems.update { previousState ->
                    val shouldShowCompleted = previousState.isDone

                    val filteredItems = items.listItems.filter { item ->
                        if (shouldShowCompleted) true
                        else item.isDone.not()
                    }
                    val completedCount = items.listItems.count { it.isDone }

                    TodoListState(
                        listItems = filteredItems,
                        completed = completedCount,
                        isDone = previousState.isDone,
                        error = items.error
                    )
                }
            }
        }
    }

    fun checkTodoItem(todoItem: TodoItem) {
        val calendar: Calendar = Calendar.getInstance()
        val checkedItem = todoItem.copy(
            isDone = todoItem.isDone.not(),
            dateOfChange = calendar.time
        )
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
                isDone = it.isDone.not(),
                error = ""
            )
        }
        loadTodoItems()
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
            loadTodoItems()
        }
    }

}
