package com.example.todolist.presentation.todo.model

import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.util.currentDate
import java.util.Date

data class TodoState(
    val content: String = "",
    val importance: Importance = Importance.BASIC,
    val deadline: Date = currentDate,
    val isDeadlineVisible: Boolean = false,
    val isAddItem: Boolean = true
)
