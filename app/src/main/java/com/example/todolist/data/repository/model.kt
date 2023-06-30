package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem

data class model(
    val listItems: List<TodoItem>,
    val error: String
) {

    companion object {

        val empty = model(
            listItems = emptyList(),
            error = ""
        )
    }
}