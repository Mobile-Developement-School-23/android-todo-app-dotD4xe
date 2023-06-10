package com.example.todolist.data.model

import com.example.todolist.util.Importance

data class TodoItem(
    val id: String,
    val content: String,
    val importance: Importance,
    val deadline: String?,
    val isDone: Boolean,
    val dateOfCreation: String,
    val dateOfChange: String?
)
