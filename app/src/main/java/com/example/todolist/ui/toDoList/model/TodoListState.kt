package com.example.todolist.ui.toDoList.model
import com.example.todolist.data.model.TodoItem

data class TodoListState(
    val listItems: List<TodoItem>,
    val completed: Int,
    val isDone: Boolean
) {

    companion object {

        val empty = TodoListState(
            listItems = emptyList(),
            completed = 0,
            isDone = false
        )
    }
}