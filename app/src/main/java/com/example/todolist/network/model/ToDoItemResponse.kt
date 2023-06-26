package com.example.todolist.network.model

data class ToDoItemResponse(
    val status: String?,
    val element: ToDoItem?,
    val revision: Int?
)