package com.example.todolist.presentation.todo

import androidx.lifecycle.ViewModel
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.domain.repository.ToDoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ToDoViewModel class is responsible for handling business logic and communication between the UI and the repository.
 * @param repository The ToDoRepository instance for data operations.
 */
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    fun addItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addItem(item)
        }
    }

    fun saveItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveItem(item)
        }
    }

    fun deleteItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteItem(item)
        }
    }
}
