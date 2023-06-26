package com.example.todolist.network.model

data class ToDoListResponse(
    val status: String?,
    val list: List<ToDoItem>?,
    val revision: Int?
)