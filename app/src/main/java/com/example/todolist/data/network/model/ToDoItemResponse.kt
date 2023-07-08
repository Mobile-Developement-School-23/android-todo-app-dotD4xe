package com.example.todolist.data.network.model

/**
 * Data class representing a Todo item response received from the API.
 */
data class ToDoItemResponse(
    val status: String,
    val element: ToDoItemDto,
    val revision: Int
)
