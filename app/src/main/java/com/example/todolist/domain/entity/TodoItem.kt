package com.example.todolist.domain.entity

import android.os.Parcelable
import com.example.todolist.data.database.entity.TodoItemEntity
import com.example.todolist.data.network.model.ToDoItemDto
import com.example.todolist.presentation.util.toUnixTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Data class representing a Todo item.
 */
@Parcelize
data class TodoItem(
    val id: String,
    val content: String,
    val importance: Importance,
    val deadline: Date?,
    val color: String?,
    var isDone: Boolean,
    val dateOfCreation: Date,
    val dateOfChange: Date,
    val lastUpdatedBy: String
): Parcelable

/**
 * Enum class representing the importance levels of a Todo item.
 */
enum class Importance {
    LOW {
        override fun toString(): String {
            return "low"
        }
    },
    BASIC {
        override fun toString(): String {
            return "basic"
        }
    },
    IMPORTANT {
        override fun toString(): String {
            return "important"
        }
    }
}

fun List<TodoItem>.toDtoList(): List<ToDoItemDto> {
    return map { todoItem ->
        ToDoItemDto(
            id = todoItem.id,
            text = todoItem.content,
            importance = todoItem.importance.toString(),
            deadline = todoItem.deadline?.toUnixTimestamp(),
            done = todoItem.isDone,
            color = todoItem.color,
            createdAt = todoItem.dateOfCreation.toUnixTimestamp(),
            changedAt = todoItem.dateOfChange.toUnixTimestamp(),
            lastUpdatedBy = todoItem.lastUpdatedBy
        )
    }
}

fun TodoItem.toEntity(): TodoItemEntity {
    return TodoItemEntity(
        id = id,
        text = content,
        importance = importance.toString(),
        deadline = deadline?.toUnixTimestamp(),
        done = isDone,
        color = color,
        created_at = dateOfCreation.toUnixTimestamp(),
        changed_at = dateOfChange.toUnixTimestamp(),
        last_updated_by = lastUpdatedBy
    )
}

fun TodoItem.toToDoItemDto(): ToDoItemDto {
    return ToDoItemDto(
        id = this.id,
        text = this.content,
        importance = this.importance.toString(),
        deadline = this.deadline?.toUnixTimestamp(),
        done = this.isDone,
        color = this.color,
        createdAt = this.dateOfCreation.toUnixTimestamp(),
        changedAt = this.dateOfChange.toUnixTimestamp(),
        lastUpdatedBy = this.lastUpdatedBy
    )
}





