package com.example.todolist.util

import com.example.todolist.data.model.TodoItem
import com.example.todolist.network.model.ToDoItemDto

fun TodoItem.toToDoItemDto(): ToDoItemDto {
    return ToDoItemDto(
        id = this.id,
        text = this.content,
        importance = this.importance.toString(),
        deadline = this.deadline?.toUnixTimestamp(),
        done = this.isDone,
        color = this.color,
        created_at = this.dateOfCreation.toUnixTimestamp(),
        changed_at = this.dateOfChange.toUnixTimestamp(),
        last_updated_by = this.lastUpdatedBy
    )
}

fun ToDoItemDto.toTodoItem(): TodoItem {
    return TodoItem(
        id = this.id,
        content = this.text,
        importance = Importance.valueOf(this.importance),
        deadline = this.deadline?.toDateFromUnixTimestamp(),
        color = this.color,
        isDone = this.done,
        dateOfCreation = this.created_at.toDateFromUnixTimestamp(),
        dateOfChange = this.changed_at.toDateFromUnixTimestamp(),
        lastUpdatedBy = this.last_updated_by
    )
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