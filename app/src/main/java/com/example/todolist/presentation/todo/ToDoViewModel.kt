package com.example.todolist.presentation.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.domain.repository.ToDoRepository
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.model.TodoState
import com.example.todolist.presentation.util.toDateFromUnixTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.UUID
import javax.inject.Inject

/**
 * ToDoViewModel class is responsible for handling business logic and communication between the UI and the repository.
 * @param repository The ToDoRepository instance for data operations.
 */
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    private val _todoState = MutableStateFlow(TodoState())
    val todoState = _todoState.asStateFlow()

    private var todoItem: TodoItem? = null

    fun onAction(action: TodoAction) {
        when(action) {
            is TodoAction.ContentChange -> _todoState.update {
                todoState.value.copy(content = action.content) }
            is TodoAction.UpdateDeadlineVisibility -> _todoState.update {
                todoState.value.copy(isDeadlineVisible = action.visible) }
            is TodoAction.UpdateImportance -> _todoState.update {
                todoState.value.copy(importance = action.importance) }
            is TodoAction.UpdateDeadline -> _todoState.update {
                todoState.value.copy(deadline = Date(action.deadline) ) }

            TodoAction.SaveTodo -> {
                if (todoState.value.isAddItem) addItem() else saveItem()
            }
            TodoAction.DeleteTodo -> deleteItem()
        }
    }

    private fun addItem() {
        val item = TodoItem(
            id = UUID.randomUUID().toString(),
            content = todoState.value.content.trim(),
            importance = todoState.value.importance,
            color = null,
            isDone = false,
            deadline = if (todoState.value.isDeadlineVisible) todoState.value.deadline else null,
            dateOfChange = Calendar.getInstance().time,
            dateOfCreation = Calendar.getInstance().time,
            lastUpdatedBy = "405"
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.addItem(item)
        }
    }

    fun saveItem() {
        val item = todoItem!!.copy(
            content = todoState.value.content,
            importance = todoState.value.importance,
            deadline = if (todoState.value.isDeadlineVisible) todoState.value.deadline else null,
            dateOfChange = Calendar.getInstance().time
        )
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveItem(item)
        }
    }

    fun deleteItem() {
        CoroutineScope(Dispatchers.IO).launch {
            if (todoItem != null) repository.deleteItem(todoItem!!)
        }
    }

    fun setupTodo(todo: TodoItem?) {
        if (todo == null) return
        viewModelScope.launch(Dispatchers.IO) {
            _todoState.update { todoState.value.copy(
                content = todo.content,
                importance = todo.importance,
                deadline = todo.deadline ?: todoState.value.deadline,
                isDeadlineVisible = todo.deadline != null,
                isAddItem = false
            ) }
            todoItem = todo
        }
    }
}
