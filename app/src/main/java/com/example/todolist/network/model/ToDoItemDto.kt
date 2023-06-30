package com.example.todolist.network.model

import com.example.todolist.database.entity.TodoItemEntity

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

fun List<ToDoItemDto>.toEntityList(): List<TodoItemEntity> {
    return map { dto ->
        TodoItemEntity(
            id = dto.id,
            text = dto.text,
            importance = dto.importance,
            deadline = dto.deadline,
            done = dto.done,
            color = dto.color,
            created_at = dto.created_at,
            changed_at = dto.changed_at,
            last_updated_by = dto.last_updated_by
        )
    }
}
