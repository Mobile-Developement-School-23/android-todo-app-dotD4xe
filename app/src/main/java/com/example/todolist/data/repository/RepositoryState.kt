package com.example.todolist.data.repository

import com.example.todolist.domain.entity.TodoItem

/**
 * Data class representing the state of a repository.
 * @property listItems The list of TodoItem objects.
 * @property error The error message associated with the repository state.
 */
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
