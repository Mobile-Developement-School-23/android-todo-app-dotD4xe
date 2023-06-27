package com.example.todolist.network.model

data class ToDoItemDto(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    val created_at: Long,
    val changed_at: Long,
    val last_updated_by: String
)
data class AddTodoRequest(val element: ToDoItemDto)
data class AddTodoRequestList(val list: List<ToDoItemDto>)
