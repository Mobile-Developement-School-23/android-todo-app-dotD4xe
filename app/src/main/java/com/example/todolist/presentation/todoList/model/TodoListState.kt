package com.example.todolist.presentation.todoList.model
import com.example.todolist.domain.entity.TodoItem

/**
 * Data class representing the state of a Todo list.
 */
data class TodoListState(
    val listItems: List<TodoItem>,
    val completed: Int,
    val isDone: Boolean,
    val error: String
) {

    companion object {
        val empty = TodoListState(
            listItems = emptyList(),
            completed = 0,
            isDone = false,
            error = ""
        )
    }
}
