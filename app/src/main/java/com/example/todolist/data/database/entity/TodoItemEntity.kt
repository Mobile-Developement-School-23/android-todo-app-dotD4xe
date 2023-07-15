package com.example.todolist.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todolist.domain.entity.Importance
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.presentation.util.toDateFromUnixTimestamp
import com.example.todolist.presentation.util.toUnixTimestamp

/**
 * Entity class representing a Todo item in the database.
 */
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

fun TodoItemEntity.toTodoItem(): TodoItem {
    return TodoItem(
        id = this.id,
        content = this.text,
        importance = Importance.valueOf(this.importance.uppercase()),
        deadline = this.deadline?.toDateFromUnixTimestamp(),
        color = this.color,
        isDone = this.done,
        dateOfCreation = this.created_at.toDateFromUnixTimestamp(),
        dateOfChange = this.changed_at.toDateFromUnixTimestamp(),
        lastUpdatedBy = this.last_updated_by
    )
}
