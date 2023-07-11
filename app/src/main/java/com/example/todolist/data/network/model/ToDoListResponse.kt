package com.example.todolist.data.network.model

/**
 * Data class representing a Todo list response received from the API.
 */
data class ToDoListResponse(
    val status: String,
    val list: List<ToDoItemDto>?,
    val revision: Int
)
