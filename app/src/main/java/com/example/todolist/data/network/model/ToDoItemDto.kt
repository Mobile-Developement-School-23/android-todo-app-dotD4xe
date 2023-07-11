package com.example.todolist.data.network.model

import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.data.database.entity.TodoItemEntity
import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.util.toDateFromUnixTimestamp
import com.google.gson.annotations.SerializedName

/**
 * Data class representing a Todo item DTO (Data Transfer Object) received from the API.
 */
data class ToDoItemDto(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long?,
    val done: Boolean,
    val color: String?,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("changed_at")
    val changedAt: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String
)

/**
 * Data class representing an add Todo item request DTO.
 * @property element The Todo item to be added.
 */
data class AddTodoRequest(val element: ToDoItemDto)

/**
 * Data class representing an add Todo item list request DTO.
 * @property list The list of Todo items to be added.
 */
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
            created_at = dto.createdAt,
            changed_at = dto.changedAt,
            last_updated_by = dto.lastUpdatedBy
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
            dateOfCreation = dto.createdAt.toDateFromUnixTimestamp(),
            dateOfChange = dto.changedAt.toDateFromUnixTimestamp(),
            lastUpdatedBy = dto.lastUpdatedBy
        )
    }
}
