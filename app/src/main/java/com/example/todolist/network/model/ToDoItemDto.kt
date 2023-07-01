package com.example.todolist.network.model

import com.example.todolist.data.model.TodoItem
import com.example.todolist.database.entity.TodoItemEntity
import com.example.todolist.util.Importance
import com.example.todolist.util.toDateFromUnixTimestamp

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

fun List<ToDoItemDto>.toTodoItemEntityList(): List<TodoItemEntity> {
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

fun List<ToDoItemDto>.toTodoItemList(): List<TodoItem> {
    return this.map { dto ->
        TodoItem(
            id = dto.id,
            content = dto.text,
            importance = Importance.valueOf(dto.importance.uppercase()),
            deadline = dto.deadline?.toDateFromUnixTimestamp(),
            color = dto.color,
            isDone = dto.done,
            dateOfCreation = dto.created_at.toDateFromUnixTimestamp(),
            dateOfChange = dto.changed_at.toDateFromUnixTimestamp(),
            lastUpdatedBy = dto.last_updated_by
        )
    }
}
