package com.example.todolist.util

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()
    data class Failure<out T>(val error: String?, val data: T): UiState<T>()
}