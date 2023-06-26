package com.example.todolist.network.model

import com.example.todolist.util.Importance
import java.util.Date

data class ToDoItem(
    val id: String?,
    val text: String?,
    val importance: String?,
    val deadline: Long?,
    val done: Boolean?,
    val color: String?,
    val created_at: Long?,
    val changed_at: Long?,
    val last_updated_by: String?
)
data class AddTodoRequest(val element: ToDoItem)
data class AddTodoRequest1(val list: List<ToDoItem>)
