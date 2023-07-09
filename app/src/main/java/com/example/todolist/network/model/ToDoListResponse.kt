package com.example.todolist.network.model

data class ToDoListResponse(
    val status: String,
    val list: List<ToDoItemDto>?,
    val revision: Int
)