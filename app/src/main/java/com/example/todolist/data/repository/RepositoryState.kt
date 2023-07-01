package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem

data class RepositoryState(
    val listItems: List<TodoItem>,
    val error: String
) {

    companion object {

        val empty = RepositoryState(
            listItems = emptyList(),
            error = ""
        )
    }
}