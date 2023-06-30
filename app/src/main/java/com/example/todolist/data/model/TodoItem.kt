package com.example.todolist.data.model

import android.os.Parcelable
import com.example.todolist.database.entity.TodoItemEntity
import com.example.todolist.network.model.ToDoItemDto
import com.example.todolist.util.Importance
import com.example.todolist.util.toDateFromUnixTimestamp
import com.example.todolist.util.toUnixTimestamp
import kotlinx.parcelize.Parcelize
import java.util.Date

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
fun TodoItemEntity.toTodoItem(): TodoItem {
    return TodoItem(
        id = id,
        content = text,
        importance = Importance.valueOf(importance.uppercase()),
        deadline = deadline?.toDateFromUnixTimestamp(),
        color = color,
        isDone = done,
        dateOfCreation = created_at.toDateFromUnixTimestamp(),
        dateOfChange = changed_at.toDateFromUnixTimestamp(),
        lastUpdatedBy = last_updated_by
    )
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
            created_at = todoItem.dateOfCreation.toUnixTimestamp(),
            changed_at = todoItem.dateOfChange.toUnixTimestamp(),
            last_updated_by = todoItem.lastUpdatedBy
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





