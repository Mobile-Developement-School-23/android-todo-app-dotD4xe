package com.example.todolist.presentation.todo.model

import com.example.todolist.domain.entity.Importance

sealed class TodoAction {
    data class ContentChange(val content: String) : TodoAction()
    data class UpdateDeadlineVisibility(val visible: Boolean): TodoAction()
    data class UpdateImportance(val importance: Importance) : TodoAction()
    data class UpdateDeadline(val deadline: Long) : TodoAction()

    object SaveTodo: TodoAction()
    object DeleteTodo : TodoAction()
}
