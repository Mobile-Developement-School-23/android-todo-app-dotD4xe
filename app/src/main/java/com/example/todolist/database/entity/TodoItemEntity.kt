package com.example.todolist.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.data.model.TodoItem
import com.example.todolist.network.model.ToDoItemDto
import com.example.todolist.util.Importance
import com.example.todolist.util.toDateFromUnixTimestamp
import com.example.todolist.util.toTodoItem

@Entity(tableName = "todo_items")
data class TodoItemEntity (
    @PrimaryKey
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

fun ToDoItemDto.toTodoItemEntity(): TodoItemEntity {
    return TodoItemEntity(
        id = id,
        text = text,
        importance = importance,
        deadline = deadline,
        done = done,
        color = color,
        created_at = created_at,
        changed_at = changed_at,
        last_updated_by = last_updated_by
    )
}

fun List<TodoItemEntity>.toTodoItemList(): List<TodoItem> {
    return map { entity ->
        TodoItem(
            id = entity.id,
            content = entity.text,
            importance = Importance.valueOf(entity.importance.uppercase()),
            deadline = entity.deadline?.toDateFromUnixTimestamp(),
            color = entity.color,
            isDone = entity.done,
            dateOfCreation = entity.created_at.toDateFromUnixTimestamp(),
            dateOfChange = entity.changed_at.toDateFromUnixTimestamp(),
            lastUpdatedBy = entity.last_updated_by
        )
    }
}
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